package restaurante_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restaurante_api.dto.AgregarItemDTO;
import restaurante_api.dto.CancelarPedidoDTO;
import restaurante_api.dto.CrearPedidoDTO;
import restaurante_api.model.DetallePedido;
import restaurante_api.model.Pedido;
import restaurante_api.service.PedidoService;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // --- ENDPOINTS ORIGINALES MANTENIDOS ---

    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody CrearPedidoDTO dto) {
        try {
            Pedido pedido = pedidoService.crearPedido(dto.getIdMesa(), dto.getIdUsuario());
            return ResponseEntity.ok(pedido);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/agregar-item")
    public ResponseEntity<?> agregarItem(@RequestBody AgregarItemDTO dto) {
        try {
            DetallePedido detalle = pedidoService.agregarItem(
                    dto.getIdPedido(), dto.getIdProducto(), dto.getCantidad(), dto.getObservaciones()
            );
            return ResponseEntity.ok(detalle);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{idPedido}/por-cobrar")
    public ResponseEntity<?> cambiarPorCobrar(@PathVariable Integer idPedido) {
        try {
            pedidoService.cambiarEstadoMesaPorCobrar(idPedido);
            return ResponseEntity.ok("Mesa marcada como POR_COBRAR");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- NUEVOS ENDPOINTS AÑADIDOS ---

    // GET: /api/pedidos (Listar todos)
    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        return ResponseEntity.ok(pedidoService.obtenerTodos());
    }

    // GET: /api/pedidos/{id} (Consultar un pedido en específico con su detalle)
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(pedidoService.obtenerPorId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE: /api/pedidos/{idPedido}/items/{idDetalle} (Eliminar un ítem)
    @DeleteMapping("/{idPedido}/items/{idDetalle}")
    public ResponseEntity<?> eliminarItem(
            @PathVariable Integer idPedido,
            @PathVariable Integer idDetalle) {
        try {
            Pedido pedidoActualizado = pedidoService.eliminarItemDetalle(idPedido, idDetalle);
            return ResponseEntity.ok(pedidoActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT: /api/pedidos/{idPedido}/cancelar (Anular pedido)
    @PutMapping("/{idPedido}/cancelar")
    public ResponseEntity<?> cancelarPedido(
            @PathVariable Integer idPedido,
            @RequestBody CancelarPedidoDTO dto) {
        try {
            Pedido pedidoCancelado = pedidoService.cancelarPedido(idPedido, dto);
            return ResponseEntity.ok(pedidoCancelado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}