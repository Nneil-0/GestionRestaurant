package restaurante_api.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CrearProductoDTO {
    private String nombre;
    private Integer idCategoria;
    private BigDecimal precio;
    private BigDecimal stockActual;
    private String tipoProducto; // 'PLATILLO', 'BEBIDA', 'INSUMO'
}
