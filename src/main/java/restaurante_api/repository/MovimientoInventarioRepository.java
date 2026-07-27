package restaurante_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restaurante_api.model.MovimientoInventario;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Integer> {
    List<MovimientoInventario> findByProducto_IdProductoOrderByFechaDesc(Integer idProducto);
    List<MovimientoInventario> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}