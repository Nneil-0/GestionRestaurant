package restaurante_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.GeneratedColumn; // <-- Importante
import java.math.BigDecimal;

@Entity
@Table(name = "DetallePedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    // Hibernate 6+: indica que el valor proviene de una columna calculada de SQL Server
    @GeneratedColumn("cantidad * precio_unitario")
    @Column(name = "subtotal", insertable = false, updatable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "observaciones", length = 150)
    private String observaciones;
}