package restaurante_api.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AperturaCajaDTO {
    private Integer idUsuario;
    private BigDecimal montoApertura;
}