package restaurante_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restaurante_api.dto.MovimientoInventarioRequestDTO;
import restaurante_api.model.MovimientoInventario;
import restaurante_api.service.InventarioService;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "*")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @PostMapping("/movimiento")
    public ResponseEntity<?> registrarMovimiento(@RequestBody MovimientoInventarioRequestDTO dto) {
        try {
            MovimientoInventario movimiento = inventarioService.registrarMovimiento(
                    dto.getIdProducto(),
                    dto.getIdUsuario(),
                    dto.getTipoMovimiento(),
                    dto.getCantidad(),
                    dto.getMotivo()
            );
            return ResponseEntity.ok(movimiento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<MovimientoInventario>> obtenerHistorial(@PathVariable Integer idProducto) {
        return ResponseEntity.ok(inventarioService.obtenerHistorialProducto(idProducto));
    }
}