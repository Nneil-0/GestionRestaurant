package restaurante_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "SesionesCaja")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SesionCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sesion")
    private Integer idSesion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "monto_apertura", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoApertura;

    @Column(name = "monto_cierre", precision = 10, scale = 2)
    private BigDecimal montoCierre;

    @Column(name = "fecha_apertura")
    private LocalDateTime fechaApertura = LocalDateTime.now();

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    @Column(name = "estado", length = 10)
    private String estado = "ABIERTA"; // 'ABIERTA', 'CERRADA'
}
