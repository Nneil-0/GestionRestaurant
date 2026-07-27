package restaurante_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restaurante_api.dto.EmitirComprobanteDTO;
import restaurante_api.model.Comprobante;
import restaurante_api.service.ComprobanteService;

@RestController
@RequestMapping("/api/comprobantes")
@CrossOrigin(origins = "*")
public class ComprobanteController {

    @Autowired
    private ComprobanteService comprobanteService;

    @PostMapping("/emitir")
    public ResponseEntity<?> emitirComprobante(@RequestBody EmitirComprobanteDTO dto) {
        try {
            Comprobante comprobante = comprobanteService.emitirComprobante(
                    dto.getIdPedido(),
                    dto.getIdSesion(),
                    dto.getIdUsuario(),
                    dto.getTipoComprobante(),
                    dto.getNumDocCliente(),
                    dto.getRazonSocial(),
                    dto.getMetodoPago(),
                    dto.getMontoRecibido()
            );
            return ResponseEntity.ok(comprobante);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}