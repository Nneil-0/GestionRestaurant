package restaurante_api.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class EmitirComprobanteDTO {
    private Integer idPedido;
    private Integer idSesion;
    private Integer idUsuario;
    private String tipoComprobante; // 'TICKET', 'BOLETA', 'FACTURA'
    private String numDocCliente;  // DNI o RUC
    private String razonSocial;
    private String metodoPago;      // 'EFECTIVO', 'YAPE', 'PLIN', 'TARJETA'
    private BigDecimal montoRecibido;
}