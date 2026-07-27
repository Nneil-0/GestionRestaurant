package restaurante_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restaurante_api.dto.CrearUsuarioDTO;
import restaurante_api.dto.UsuarioResponseDTO;
import restaurante_api.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // GET: /api/usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    // GET: /api/usuarios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    // POST: /api/usuarios
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@RequestBody CrearUsuarioDTO dto) {
        UsuarioResponseDTO nuevoUsuario = usuarioService.crear(dto);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    // PATCH: /api/usuarios/{id}/estado?estado=true|false
    @PatchMapping("/{id}/estado")
    public ResponseEntity<UsuarioResponseDTO> cambiarEstado(
            @PathVariable Integer id,
            @RequestParam Boolean estado) {
        UsuarioResponseDTO actualizado = usuarioService.cambiarEstado(id, estado);
        return ResponseEntity.ok(actualizado);
    }
}