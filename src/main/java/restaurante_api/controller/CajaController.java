package restaurante_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restaurante_api.dto.AperturaCajaDTO;
import restaurante_api.dto.CierreCajaDTO;
import restaurante_api.model.SesionCaja;
import restaurante_api.service.CajaService;

@RestController
@RequestMapping("/api/caja")
@CrossOrigin(origins = "*")
public class CajaController {

    @Autowired
    private CajaService cajaService;

    @PostMapping("/abrir")
    public ResponseEntity<?> abrirCaja(@RequestBody AperturaCajaDTO dto) {
        try {
            SesionCaja sesion = cajaService.abrirCaja(dto.getIdUsuario(), dto.getMontoApertura());
            return ResponseEntity.ok(sesion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cerrar")
    public ResponseEntity<?> cerrarCaja(@RequestBody CierreCajaDTO dto) {
        try {
            SesionCaja sesion = cajaService.cerrarCaja(dto.getIdSesion(), dto.getMontoCierre());
            return ResponseEntity.ok(sesion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/activa/usuario/{idUsuario}")
    public ResponseEntity<?> obtenerSesionActiva(@PathVariable Integer idUsuario) {
        try {
            SesionCaja sesion = cajaService.obtenerSesionActivaUsuario(idUsuario);
            return ResponseEntity.ok(sesion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}