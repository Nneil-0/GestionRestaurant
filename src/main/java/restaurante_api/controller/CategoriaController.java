package restaurante_api.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import restaurante_api.model.Categoria;
import restaurante_api.service.CategoriaService;


@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // GET: /api/categorias
    // Si pasas ?soloActivas=true, devuelve solo las activas
    @GetMapping
    public ResponseEntity<List<Categoria>> listarCategorias(
            @RequestParam(required = false, defaultValue = "false") boolean soloActivas) {
        if (soloActivas) {
            return ResponseEntity.ok(categoriaService.obtenerActivas());
        }
        return ResponseEntity.ok(categoriaService.obtenerTodas());
    }

    // GET: /api/categorias/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(categoriaService.obtenerPorId(id));
    }

    // POST: /api/categorias
    @PostMapping
    public ResponseEntity<Categoria> crear(@RequestBody Categoria categoria) {
        Categoria nuevaCategoria = categoriaService.crear(categoria);
        return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
    }

    // PUT: /api/categorias/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable Integer id, @RequestBody Categoria categoria) {
        Categoria actualizada = categoriaService.actualizar(id, categoria);
        return ResponseEntity.ok(actualizada);
    }

    // PATCH: /api/categorias/{id}/estado?estado=true|false
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Categoria> cambiarEstado(@PathVariable Integer id, @RequestParam Boolean estado) {
        Categoria actualizada = categoriaService.cambiarEstado(id, estado);
        return ResponseEntity.ok(actualizada);
    }
}