package restaurante_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer idPedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_mesa", nullable = true) // NULL si es pedido para llevar
    private Mesa mesa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora = LocalDateTime.now();

    @Column(name = "estado", length = 20)
    private String estado = "PENDIENTE"; // 'PENDIENTE', 'EN_PROCESO', 'PAGADO', 'CANCELADO'

    @Column(name = "motivo_cancelacion", length = 200)
    private String motivoCancelacion;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;
}