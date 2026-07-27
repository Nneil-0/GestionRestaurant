package restaurante_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restaurante_api.model.SesionCaja;

import java.util.Optional;

@Repository
public interface SesionCajaRepository extends JpaRepository<SesionCaja, Integer> {
    // Busca la sesión abierta para un usuario específico (Garantiza 1 caja abierta por turno)
    Optional<SesionCaja> findByUsuario_IdUsuarioAndEstado(Integer idUsuario, String estado);
}