package restaurante_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restaurante_api.dto.CrearProductoDTO;
import restaurante_api.model.Categoria;
import restaurante_api.model.Producto;
import restaurante_api.repository.CategoriaRepository;
import restaurante_api.repository.ProductoRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepo;

    @Autowired
    private CategoriaRepository categoriaRepo;

    // Listar productos activos (para el POS / Menú)
    public List<Producto> obtenerActivos() {
        return productoRepo.findByEstadoTrue();
    }

    // Listar todos los productos (incluyendo inactivos para el panel de administración)
    public List<Producto> obtenerTodos() {
        return productoRepo.findAll();
    }

    // Buscar por ID
    public Producto obtenerPorId(Integer id) {
        return productoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con el ID: " + id));
    }

    // Listar por categoría
    public List<Producto> obtenerPorCategoria(Integer idCategoria) {
        return productoRepo.findByCategoria_IdCategoriaAndEstadoTrue(idCategoria);
    }

    // Crear producto
    public Producto crear(CrearProductoDTO dto) {
        Categoria categoria = categoriaRepo.findById(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + dto.getIdCategoria()));

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setCategoria(categoria);
        producto.setPrecio(dto.getPrecio());
        producto.setStockActual(dto.getStockActual() != null ? dto.getStockActual() : BigDecimal.ZERO);
        producto.setTipoProducto(dto.getTipoProducto());
        producto.setEstado(true);

        return productoRepo.save(producto);
    }

    // Actualizar producto completo
    public Producto actualizar(Integer id, CrearProductoDTO dto) {
        Producto producto = obtenerPorId(id);
        Categoria categoria = categoriaRepo.findById(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + dto.getIdCategoria()));

        producto.setNombre(dto.getNombre());
        producto.setCategoria(categoria);
        producto.setPrecio(dto.getPrecio());
        if (dto.getStockActual() != null) {
            producto.setStockActual(dto.getStockActual());
        }
        producto.setTipoProducto(dto.getTipoProducto());

        return productoRepo.save(producto);
    }

    // Cambiar estado (Activar / Inactivar)
    public Producto cambiarEstado(Integer id, Boolean nuevoEstado) {
        Producto producto = obtenerPorId(id);
        producto.setEstado(nuevoEstado);
        return productoRepo.save(producto);
    }

    // Actualizar stock directamente
    public Producto actualizarStock(Integer id, BigDecimal nuevoStock) {
        Producto producto = obtenerPorId(id);
        producto.setStockActual(nuevoStock);
        return productoRepo.save(producto);
    }
}