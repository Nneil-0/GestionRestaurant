package restaurante_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restaurante_api.model.Pedido;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByEstado(String estado);
    Optional<Pedido> findByMesa_IdMesaAndEstadoNot(Integer idMesa, String estado);
}