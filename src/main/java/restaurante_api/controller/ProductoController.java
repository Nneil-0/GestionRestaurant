package restaurante_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restaurante_api.dto.ActualizarStockDTO;
import restaurante_api.dto.CrearProductoDTO;
import restaurante_api.model.Producto;
import restaurante_api.service.ProductoService;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // GET: /api/productos
    // Si pasas ?soloActivos=true devuelve solo los activos (por defecto true)
    @GetMapping
    public ResponseEntity<List<Producto>> listarTodos(
            @RequestParam(required = false, defaultValue = "true") boolean soloActivos) {
        if (soloActivos) {
            return ResponseEntity.ok(productoService.obtenerActivos());
        }
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    // GET: /api/productos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    // GET: /api/productos/categoria/{idCategoria}
    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<Producto>> listarPorCategoria(@PathVariable Integer idCategoria) {
        return ResponseEntity.ok(productoService.obtenerPorCategoria(idCategoria));
    }

    // POST: /api/productos
    @PostMapping
    public ResponseEntity<Producto> guardar(@RequestBody CrearProductoDTO dto) {
        Producto nuevoProducto = productoService.crear(dto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    // PUT: /api/productos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Integer id, @RequestBody CrearProductoDTO dto) {
        Producto productoActualizado = productoService.actualizar(id, dto);
        return ResponseEntity.ok(productoActualizado);
    }

    // PATCH: /api/productos/{id}/estado?estado=true|false
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Producto> cambiarEstado(@PathVariable Integer id, @RequestParam Boolean estado) {
        Producto productoActualizado = productoService.cambiarEstado(id, estado);
        return ResponseEntity.ok(productoActualizado);
    }

    // PATCH: /api/productos/{id}/stock
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Producto> actualizarStock(@PathVariable Integer id, @RequestBody ActualizarStockDTO dto) {
        Producto productoActualizado = productoService.actualizarStock(id, dto.getStock());
        return ResponseEntity.ok(productoActualizado);
    }
}