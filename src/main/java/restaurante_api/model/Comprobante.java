package restaurante_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Comprobantes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comprobante")
    private Integer idComprobante;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pedido", nullable = false, unique = true)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_sesion", nullable = false)
    private SesionCaja sesionCaja;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "tipo_comprobante", nullable = false, length = 20)
    private String tipoComprobante; // 'TICKET', 'BOLETA', 'FACTURA'

    @Column(name = "serie", nullable = false, length = 5)
    private String serie; // 'T001', 'B001', 'F001'

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "num_documento_cliente", length = 15)
    private String numDocumentoCliente; // DNI / RUC

    @Column(name = "razon_social", length = 150)
    private String razonSocial;

    @Column(name = "metodo_pago", nullable = false, length = 20)
    private String metodoPago; // 'EFECTIVO', 'YAPE', 'PLIN', 'TARJETA'

    @Column(name = "monto_subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoSubtotal;

    @Column(name = "monto_igv", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoIgv; // 18% para Boleta/Factura, 0.00 para Ticket

    @Column(name = "monto_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Column(name = "monto_recibido", precision = 10, scale = 2)
    private BigDecimal montoRecibido;

    @Column(name = "vuelto", precision = 10, scale = 2)
    private BigDecimal vuelto;

    @Column(name = "fecha_emision")
    private LocalDateTime fechaEmision = LocalDateTime.now();
}