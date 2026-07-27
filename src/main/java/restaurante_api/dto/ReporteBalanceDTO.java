package restaurante_api.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReporteBalanceDTO {
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private BigDecimal totalIngresosVentas;
    private BigDecimal totalEgresosInventario;
    private BigDecimal gananciaNeta;
    private Long cantidadVentasRealizadas;
}