package restaurante_api.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private String usuario;
    private String contrasena;
}