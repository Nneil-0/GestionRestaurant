package restaurante_api.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ActualizarStockDTO {
    private BigDecimal stock;
}