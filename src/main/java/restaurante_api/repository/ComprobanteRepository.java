package restaurante_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restaurante_api.model.Comprobante;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComprobanteRepository extends JpaRepository<Comprobante, Integer> {
    Optional<Comprobante> findByPedido_IdPedido(Integer idPedido);
    List<Comprobante> findByFechaEmisionBetween(LocalDateTime inicio, LocalDateTime fin);

    // Obtiene el último número emitido por serie para autoincrementar correlativo local (ej. B001-000102)
    Optional<Comprobante> findTopByTipoComprobanteOrderByNumeroDesc(String tipoComprobante);
}