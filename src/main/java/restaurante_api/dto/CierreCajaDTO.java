package restaurante_api.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CierreCajaDTO {
    private Integer idSesion;
    private BigDecimal montoCierre;
}