package restaurante_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restaurante_api.dto.ReporteBalanceDTO;
import restaurante_api.model.Comprobante;
import restaurante_api.model.MovimientoInventario;
import restaurante_api.repository.ComprobanteRepository;
import restaurante_api.repository.MovimientoInventarioRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReporteService {

    @Autowired
    private ComprobanteRepository comprobanteRepo;

    @Autowired
    private MovimientoInventarioRepository movimientoRepo;

    public ReporteBalanceDTO obtenerBalancePorRango(LocalDateTime inicio, LocalDateTime fin) {
        List<Comprobante> comprobantes = comprobanteRepo.findByFechaEmisionBetween(inicio, fin);
        List<MovimientoInventario> movimientos = movimientoRepo.findByFechaBetween(inicio, fin);

        // Sumar ingresos por ventas de comprobantes
        BigDecimal totalIngresos = comprobantes.stream()
                .map(Comprobante::getMontoTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Corregido: Cantidad * Precio unitario del producto para obtener el monto en Soles
        BigDecimal totalEgresos = movimientos.stream()
                .filter(m -> "EGRESO".equalsIgnoreCase(m.getTipoMovimiento()))
                .map(m -> m.getCantidad().multiply(m.getProducto().getPrecio()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal gananciaNeta = totalIngresos.subtract(totalEgresos);

        ReporteBalanceDTO reporte = new ReporteBalanceDTO();
        reporte.setFechaInicio(inicio);
        reporte.setFechaFin(fin);
        reporte.setTotalIngresosVentas(totalIngresos);
        reporte.setTotalEgresosInventario(totalEgresos);
        reporte.setGananciaNeta(gananciaNeta);
        reporte.setCantidadVentasRealizadas((long) comprobantes.size());

        return reporte;
    }

    public ReporteBalanceDTO obtenerBalanceDelDia() {
        LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
        LocalDateTime finDia = LocalDate.now().atTime(LocalTime.MAX);
        return obtenerBalancePorRango(inicioDia, finDia);
    }
}