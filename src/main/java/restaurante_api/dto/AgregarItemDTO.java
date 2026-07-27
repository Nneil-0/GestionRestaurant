package restaurante_api.dto;

import lombok.Data;

@Data
public class AgregarItemDTO {
    private Integer idPedido;
    private Integer idProducto;
    private Integer cantidad;
    private String observaciones;
}