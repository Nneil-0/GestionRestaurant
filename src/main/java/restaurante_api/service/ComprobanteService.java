package restaurante_api.service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurante_api.model.*;
import restaurante_api.repository.*;

@Service
public class ComprobanteService {

    @Autowired
    private ComprobanteRepository comprobanteRepo;

    @Autowired
    private PedidoRepository pedidoRepo;

    @Autowired
    private SesionCajaRepository sesionCajaRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private MesaRepository mesaRepo;

    @Autowired
    private DetallePedidoRepository detalleRepo;

    @Autowired
    private InventarioService inventarioService;

    @Transactional
    public Comprobante emitirComprobante(Integer idPedido, Integer idSesion, Integer idUsuario,
                                         String tipoComprobante, String numDocCliente, String razonSocial,
                                         String metodoPago, BigDecimal montoRecibido) {

        Pedido pedido = pedidoRepo.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if ("PAGADO".equalsIgnoreCase(pedido.getEstado())) {
            throw new IllegalStateException("El pedido ya fue pagado previamente");
        }

        SesionCaja sesion = sesionCajaRepo.findById(idSesion)
                .orElseThrow(() -> new RuntimeException("Sesión de caja no encontrada"));

        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        BigDecimal total = pedido.getTotal();
        BigDecimal subtotal;
        BigDecimal igv;

        // Regla Fiscal Peruana: Desglose de 18% IGV
        if ("BOLETA".equalsIgnoreCase(tipoComprobante) || "FACTURA".equalsIgnoreCase(tipoComprobante)) {
            subtotal = total.divide(new BigDecimal("1.18"), 2, RoundingMode.HALF_UP);
            igv = total.subtract(subtotal);
        } else {
            // TICKET Interno
            subtotal = total;
            igv = BigDecimal.ZERO;
        }

        // Correlativo Serie y Número Local
        String serie = switch (tipoComprobante.toUpperCase()) {
            case "BOLETA" -> "B001";
            case "FACTURA" -> "F001";
            default -> "T001";
        };

        Integer correlativo = comprobanteRepo.findTopByTipoComprobanteOrderByNumeroDesc(tipoComprobante.toUpperCase())
                .map(c -> c.getNumero() + 1)
                .orElse(1);

        BigDecimal vuelto = BigDecimal.ZERO;
        if ("EFECTIVO".equalsIgnoreCase(metodoPago) && montoRecibido != null) {
            if (montoRecibido.compareTo(total) < 0) {
                throw new IllegalArgumentException("El monto recibido es menor al total a pagar");
            }
            vuelto = montoRecibido.subtract(total);
        }

        Comprobante comprobante = new Comprobante();
        comprobante.setPedido(pedido);
        comprobante.setSesionCaja(sesion);
        comprobante.setUsuario(usuario);
        comprobante.setTipoComprobante(tipoComprobante.toUpperCase());
        comprobante.setSerie(serie);
        comprobante.setNumero(correlativo);
        comprobante.setNumDocumentoCliente(numDocCliente);
        comprobante.setRazonSocial(razonSocial);
        comprobante.setMetodoPago(metodoPago.toUpperCase());
        comprobante.setMontoSubtotal(subtotal);
        comprobante.setMontoIgv(igv);
        comprobante.setMontoTotal(total);
        comprobante.setMontoRecibido(montoRecibido);
        comprobante.setVuelto(vuelto);

        Comprobante guardado = comprobanteRepo.save(comprobante);

        // Descontar Stock automático en Kardex por los ítems del pedido
        List<DetallePedido> detalles = detalleRepo.findByPedido_IdPedido(idPedido);
        for (DetallePedido det : detalles) {
            inventarioService.registrarMovimiento(
                    det.getProducto().getIdProducto(),
                    idUsuario,
                    "EGRESO",
                    new BigDecimal(det.getCantidad()),
                    "Venta en Comprobante " + serie + "-" + correlativo
            );
        }

        // Marcar Pedido como PAGADO y Liberar Mesa
        pedido.setEstado("PAGADO");
        pedidoRepo.save(pedido);

        if (pedido.getMesa() != null) {
            Mesa mesa = pedido.getMesa();
            mesa.setEstado("LIBRE");
            mesaRepo.save(mesa);
        }

        return guardado;
    }
}