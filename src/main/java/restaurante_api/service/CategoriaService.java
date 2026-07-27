package restaurante_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restaurante_api.model.Categoria;
import restaurante_api.repository.CategoriaRepository;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Listar todas las categorías (activas e inactivas)
    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    // Listar únicamente las categorías activas (útil para la carta/menú)
    public List<Categoria> obtenerActivas() {
        return categoriaRepository.findAll().stream()
                .filter(c -> c.getEstado() != null && c.getEstado())
                .toList();
    }

    // Buscar una categoría por ID
    public Categoria obtenerPorId(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con el ID: " + id));
    }

    // Crear una nueva categoría
    public Categoria crear(Categoria categoria) {
        categoria.setEstado(true); // Se crea activa por defecto
        return categoriaRepository.save(categoria);
    }

    // Actualizar una categoría existente
    public Categoria actualizar(Integer id, Categoria categoriaDetalles) {
        Categoria categoria = obtenerPorId(id);
        categoria.setNombre(categoriaDetalles.getNombre());
        categoria.setDescripcion(categoriaDetalles.getDescripcion());
        return categoriaRepository.save(categoria);
    }

    // Cambiar estado (Activar/Inactivar)
    public Categoria cambiarEstado(Integer id, Boolean nuevoEstado) {
        Categoria categoria = obtenerPorId(id);
        categoria.setEstado(nuevoEstado);
        return categoriaRepository.save(categoria);
    }
}
