package restaurante_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurante_api.model.SesionCaja;
import restaurante_api.model.Usuario;
import restaurante_api.repository.SesionCajaRepository;
import restaurante_api.repository.UsuarioRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CajaService {

    @Autowired
    private SesionCajaRepository sesionCajaRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Transactional
    public SesionCaja abrirCaja(Integer idUsuario, BigDecimal montoApertura) {
        // Validar si ya existe una sesión abierta para el usuario
        sesionCajaRepo.findByUsuario_IdUsuarioAndEstado(idUsuario, "ABIERTA")
                .ifPresent(s -> {
                    throw new IllegalStateException("El usuario ya tiene una caja abierta actualmente");
                });

        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        SesionCaja sesion = new SesionCaja();
        sesion.setUsuario(usuario);
        sesion.setMontoApertura(montoApertura);
        sesion.setEstado("ABIERTA");

        return sesionCajaRepo.save(sesion);
    }

    @Transactional
    public SesionCaja cerrarCaja(Integer idSesion, BigDecimal montoCierre) {
        SesionCaja sesion = sesionCajaRepo.findById(idSesion)
                .orElseThrow(() -> new RuntimeException("Sesión de caja no encontrada con ID: " + idSesion));

        if ("CERRADA".equalsIgnoreCase(sesion.getEstado())) {
            throw new IllegalStateException("La sesión de caja ya se encuentra cerrada");
        }

        sesion.setMontoCierre(montoCierre);
        sesion.setFechaCierre(LocalDateTime.now());
        sesion.setEstado("CERRADA");

        return sesionCajaRepo.save(sesion);
    }

    public SesionCaja obtenerSesionActivaUsuario(Integer idUsuario) {
        return sesionCajaRepo.findByUsuario_IdUsuarioAndEstado(idUsuario, "ABIERTA")
                .orElseThrow(() -> new RuntimeException("No hay una caja abierta para el usuario ID: " + idUsuario));
    }
}