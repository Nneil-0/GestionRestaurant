package restaurante_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurante_api.model.MovimientoInventario;
import restaurante_api.model.Producto;
import restaurante_api.model.Usuario;
import restaurante_api.repository.MovimientoInventarioRepository;
import restaurante_api.repository.ProductoRepository;
import restaurante_api.repository.UsuarioRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InventarioService {

    @Autowired
    private MovimientoInventarioRepository movimientoRepo;

    @Autowired
    private ProductoRepository productoRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Transactional
    public MovimientoInventario registrarMovimiento(Integer idProducto, Integer idUsuario, String tipoMovimiento, BigDecimal cantidad, String motivo) {
        Producto producto = productoRepo.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + idProducto));

        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        if (cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }

        // Actualizar Stock Actual en la tabla Productos
        if ("INGRESO".equalsIgnoreCase(tipoMovimiento)) {
            producto.setStockActual(producto.getStockActual().add(cantidad));
        } else if ("EGRESO".equalsIgnoreCase(tipoMovimiento)) {
            if (producto.getStockActual().compareTo(cantidad) < 0) {
                throw new IllegalStateException("Stock insuficiente para realizar el egreso");
            }
            producto.setStockActual(producto.getStockActual().subtract(cantidad));
        } else {
            throw new IllegalArgumentException("Tipo de movimiento inválido. Debe ser INGRESO o EGRESO");
        }

        productoRepo.save(producto);

        // Guardar Registro en el Kardex
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setUsuario(usuario);
        movimiento.setTipoMovimiento(tipoMovimiento.toUpperCase());
        movimiento.setCantidad(cantidad);
        movimiento.setMotivo(motivo);

        return movimientoRepo.save(movimiento);
    }

    public List<MovimientoInventario> obtenerHistorialProducto(Integer idProducto) {
        return movimientoRepo.findByProducto_IdProductoOrderByFechaDesc(idProducto);
    }
}