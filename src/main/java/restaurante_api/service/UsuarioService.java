package restaurante_api.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restaurante_api.dto.CrearUsuarioDTO;
import restaurante_api.dto.LoginDTO;
import restaurante_api.dto.UsuarioResponseDTO;
import restaurante_api.model.Usuario;
import restaurante_api.repository.UsuarioRepository;

import java.util.List;
@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepo;

    // Convertir entidad Usuario a UsuarioResponseDTO
    private UsuarioResponseDTO mapearAResponseDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setUsuario(usuario.getUsuario());
        dto.setRol(usuario.getRol());
        dto.setEstado(usuario.getEstado());
        return dto;
    }

    // Autenticación de Usuario (Login)
    public UsuarioResponseDTO login(LoginDTO dto) {
        Usuario usuario = usuarioRepo.findByUsuario(dto.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario o contraseña incorrectos"));

        // Verificación simple de contraseña (sin BCrypt por simplicidad local)
        if (!usuario.getContrasenaHash().equals(dto.getContrasena())) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        if (usuario.getEstado() != null && !usuario.getEstado()) {
            throw new RuntimeException("El usuario se encuentra inactivo");
        }

        return mapearAResponseDTO(usuario);
    }

    // Listar todos los usuarios
    public List<UsuarioResponseDTO> obtenerTodos() {
        return usuarioRepo.findAll().stream()
                .map(this::mapearAResponseDTO)
                .toList();
    }

    // Buscar por ID
    public UsuarioResponseDTO obtenerPorId(Integer id) {
        Usuario usuario = usuarioRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        return mapearAResponseDTO(usuario);
    }

    // Crear un nuevo usuario
    public UsuarioResponseDTO crear(CrearUsuarioDTO dto) {
        if (usuarioRepo.findByUsuario(dto.getUsuario()).isPresent()) {
            throw new RuntimeException("El nombre de usuario '" + dto.getUsuario() + "' ya existe");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setUsuario(dto.getUsuario());
        usuario.setContrasenaHash(dto.getContrasena());
        usuario.setRol(dto.getRol().toUpperCase());
        usuario.setEstado(true);

        Usuario guardado = usuarioRepo.save(usuario);
        return mapearAResponseDTO(guardado);
    }

    // Cambiar estado (Activar / Inactivar)
    public UsuarioResponseDTO cambiarEstado(Integer id, Boolean nuevoEstado) {
        Usuario usuario = usuarioRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        usuario.setEstado(nuevoEstado);
        Usuario guardado = usuarioRepo.save(usuario);
        return mapearAResponseDTO(guardado);
    }
}
