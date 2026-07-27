package restaurante_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restaurante_api.dto.CrearMesaDTO;
import restaurante_api.model.Mesa;
import restaurante_api.service.MesaService;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
@CrossOrigin(origins = "*")
public class MesaController {

    @Autowired
    private MesaService mesaService;

    // GET: /api/mesas
    @GetMapping
    public ResponseEntity<List<Mesa>> listarTodas() {
        return ResponseEntity.ok(mesaService.obtenerTodas());
    }

    // GET: /api/mesas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Mesa> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(mesaService.obtenerPorId(id));
    }

    // GET: /api/mesas/estado/{estado} (Ej: /api/mesas/estado/LIBRE)
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Mesa>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(mesaService.obtenerPorEstado(estado));
    }

    // POST: /api/mesas
    @PostMapping
    public ResponseEntity<Mesa> crear(@RequestBody CrearMesaDTO dto) {
        Mesa nuevaMesa = mesaService.crear(dto);
        return new ResponseEntity<>(nuevaMesa, HttpStatus.CREATED);
    }

    // PUT: /api/mesas/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Mesa> actualizar(@PathVariable Integer id, @RequestBody CrearMesaDTO dto) {
        Mesa mesaActualizada = mesaService.actualizar(id, dto);
        return ResponseEntity.ok(mesaActualizada);
    }

    // PATCH: /api/mesas/{id}/estado?nuevoEstado=LIBRE|OCUPADA|POR_COBRAR
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Mesa> cambiarEstado(
            @PathVariable Integer id,
            @RequestParam String nuevoEstado) {
        Mesa mesaActualizada = mesaService.cambiarEstado(id, nuevoEstado);
        return ResponseEntity.ok(mesaActualizada);
    }
}