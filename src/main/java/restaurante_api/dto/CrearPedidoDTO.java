package restaurante_api.dto;

import lombok.Data;

@Data
public class CrearPedidoDTO {
    private Integer idMesa; // Opcional (null para llevar)
    private Integer idUsuario;
}
