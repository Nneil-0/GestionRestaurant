package restaurante_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restaurante_api.dto.LoginDTO;
import restaurante_api.dto.UsuarioResponseDTO;
import restaurante_api.service.UsuarioService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    // POST: /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(@RequestBody LoginDTO dto) {
        UsuarioResponseDTO usuarioLogueado = usuarioService.login(dto);
        return ResponseEntity.ok(usuarioLogueado);
    }
}