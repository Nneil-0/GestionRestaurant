package restaurante_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Mesas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mesa")
    private Integer idMesa;

    @Column(name = "numero_mesa", nullable = false, unique = true, length = 10)
    private String numeroMesa;

    @Column(name = "capacidad")
    private Integer capacidad = 4;

    @Column(name = "estado", length = 20)
    private String estado = "LIBRE"; // 'LIBRE', 'OCUPADA', 'POR_COBRAR'
}