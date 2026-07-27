package restaurante_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restaurante_api.dto.CrearMesaDTO;
import restaurante_api.model.Mesa;
import restaurante_api.repository.MesaRepository;

import java.util.List;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepo;

    // Listar todas las mesas
    public List<Mesa> obtenerTodas() {
        return mesaRepo.findAll();
    }

    // Listar por estado ('LIBRE', 'OCUPADA', 'POR_COBRAR')
    public List<Mesa> obtenerPorEstado(String estado) {
        return mesaRepo.findByEstado(estado.toUpperCase());
    }

    // Buscar por ID
    public Mesa obtenerPorId(Integer id) {
        return mesaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada con ID: " + id));
    }

    // Crear una nueva mesa
    public Mesa crear(CrearMesaDTO dto) {
        Mesa mesa = new Mesa();
        mesa.setNumeroMesa(dto.getNumeroMesa());
        mesa.setCapacidad(dto.getCapacidad() != null ? dto.getCapacidad() : 4);
        mesa.setEstado("LIBRE"); // Estado inicial por defecto

        return mesaRepo.save(mesa);
    }

    // Actualizar datos de la mesa (Número / Capacidad)
    public Mesa actualizar(Integer id, CrearMesaDTO dto) {
        Mesa mesa = obtenerPorId(id);
        mesa.setNumeroMesa(dto.getNumeroMesa());
        if (dto.getCapacidad() != null) {
            mesa.setCapacidad(dto.getCapacidad());
        }
        return mesaRepo.save(mesa);
    }

    // Cambiar estado manualmente ('LIBRE', 'OCUPADA', 'POR_COBRAR')
    public Mesa cambiarEstado(Integer id, String nuevoEstado) {
        Mesa mesa = obtenerPorId(id);

        String estadoUpper = nuevoEstado.toUpperCase();
        if (!estadoUpper.equals("LIBRE") && !estadoUpper.equals("OCUPADA") && !estadoUpper.equals("POR_COBRAR")) {
            throw new IllegalArgumentException("Estado no válido. Usar: LIBRE, OCUPADA o POR_COBRAR");
        }

        mesa.setEstado(estadoUpper);
        return mesaRepo.save(mesa);
    }
}