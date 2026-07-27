package restaurante_api.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MovimientoInventarioRequestDTO {
    private Integer idProducto;
    private Integer idUsuario;
    private String tipoMovimiento; // 'INGRESO' o 'EGRESO'
    private BigDecimal cantidad;
    private String motivo;
}