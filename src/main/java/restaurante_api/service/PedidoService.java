package restaurante_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurante_api.dto.CancelarPedidoDTO;
import restaurante_api.model.*;
import restaurante_api.repository.*;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepo;

    @Autowired
    private DetallePedidoRepository detalleRepo;

    @Autowired
    private MesaRepository mesaRepo;

    @Autowired
    private ProductoRepository productoRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    // --- MÉTODOS ORIGINALES MANTENIDOS ---

    @Transactional
    public Pedido crearPedido(Integer idMesa, Integer idUsuario) {
        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Mesa mesa = null;
        if (idMesa != null) {
            mesa = mesaRepo.findById(idMesa)
                    .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
            mesa.setEstado("OCUPADA");
            mesaRepo.save(mesa);
        }

        Pedido pedido = new Pedido();
        pedido.setMesa(mesa);
        pedido.setUsuario(usuario);
        pedido.setEstado("PENDIENTE");

        return pedidoRepo.save(pedido);
    }

    @Transactional
    public DetallePedido agregarItem(Integer idPedido, Integer idProducto, Integer cantidad, String observaciones) {
        Pedido pedido = pedidoRepo.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        Producto producto = productoRepo.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(pedido);
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(producto.getPrecio());
        detalle.setObservaciones(observaciones);

        DetallePedido guardado = detalleRepo.save(detalle);

        // Refrescar entidad pedido para reflejar el total recalculado por el Trigger de SQL Server
        pedidoRepo.flush();

        return guardado;
    }

    @Transactional
    public void cambiarEstadoMesaPorCobrar(Integer idPedido) {
        Pedido pedido = pedidoRepo.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (pedido.getMesa() != null) {
            Mesa mesa = pedido.getMesa();
            mesa.setEstado("POR_COBRAR");
            mesaRepo.save(mesa);
        }
    }

    // --- NUEVOS MÉTODOS AÑADIDOS PARA COMPLETAR EL MÓDULO ---

    // 1. Obtener todos los pedidos
    public List<Pedido> obtenerTodos() {
        return pedidoRepo.findAll();
    }

    // 2. Obtener pedido por ID
    public Pedido obtenerPorId(Integer id) {
        return pedidoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
    }

    // 3. Eliminar un ítem específico del detalle (error al comandar)
    @Transactional
    public Pedido eliminarItemDetalle(Integer idPedido, Integer idDetalle) {
        Pedido pedido = obtenerPorId(idPedido);

        if ("PAGADO".equalsIgnoreCase(pedido.getEstado()) || "CANCELADO".equalsIgnoreCase(pedido.getEstado())) {
            throw new RuntimeException("No se pueden modificar ítems de un pedido PAGADO o CANCELADO");
        }

        DetallePedido detalle = detalleRepo.findById(idDetalle)
                .orElseThrow(() -> new RuntimeException("Detalle de pedido no encontrado con ID: " + idDetalle));

        if (!detalle.getPedido().getIdPedido().equals(idPedido)) {
            throw new RuntimeException("El ítem indicado no pertenece al pedido especificado");
        }

        detalleRepo.delete(detalle);

        // El Trigger TRG_ActualizarTotalPedido en SQL Server recalcula el total automáticamente
        pedidoRepo.flush();

        return obtenerPorId(idPedido);
    }

    // 4. Cancelar pedido completo registrando el motivo y liberando la mesa
    @Transactional
    public Pedido cancelarPedido(Integer idPedido, CancelarPedidoDTO dto) {
        Pedido pedido = obtenerPorId(idPedido);

        if ("PAGADO".equalsIgnoreCase(pedido.getEstado())) {
            throw new RuntimeException("No se puede cancelar un pedido que ya ha sido PAGADO");
        }

        if (dto.getMotivoCancelacion() == null || dto.getMotivoCancelacion().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar un motivo para cancelar el pedido");
        }

        pedido.setEstado("CANCELADO");
        pedido.setMotivoCancelacion(dto.getMotivoCancelacion());

        // Si el pedido estaba asignado a una mesa, se vuelve a liberar
        if (pedido.getMesa() != null) {
            Mesa mesa = pedido.getMesa();
            mesa.setEstado("LIBRE");
            mesaRepo.save(mesa);
        }

        return pedidoRepo.save(pedido);
    }
}