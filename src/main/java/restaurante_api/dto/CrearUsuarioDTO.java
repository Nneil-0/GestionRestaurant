package restaurante_api.dto;

import lombok.Data;

@Data
public class CrearUsuarioDTO {
    private String nombre;
    private String usuario;
    private String contrasena;
    private String rol; // 'ADMIN', 'CAJERO', 'MESERO', 'COCINA'
}