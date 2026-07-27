package restaurante_api.dto;

import lombok.Data;

@Data
public class CrearMesaDTO {
    private String numeroMesa;
    private Integer capacidad;
}