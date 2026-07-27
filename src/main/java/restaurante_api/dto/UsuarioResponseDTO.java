package restaurante_api.dto;

import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Integer idUsuario;
    private String nombre;
    private String usuario;
    private String rol;
    private Boolean estado;
}