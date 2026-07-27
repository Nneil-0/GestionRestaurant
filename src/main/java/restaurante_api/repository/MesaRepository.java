package restaurante_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restaurante_api.model.Mesa;

import java.util.List;
import java.util.Optional;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Integer> {
    Optional<Mesa> findByNumeroMesa(String numeroMesa);
    List<Mesa> findByEstado(String estado);
}