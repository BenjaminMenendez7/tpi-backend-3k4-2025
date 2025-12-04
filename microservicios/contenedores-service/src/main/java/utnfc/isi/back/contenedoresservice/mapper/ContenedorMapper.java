package utnfc.isi.back.contenedoresservice.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import utnfc.isi.back.contenedoresservice.entity.Contenedor;
import utnfc.isi.back.contenedoresservice.dto.ContenedorDTO;
import utnfc.isi.back.contenedoresservice.entity.EstadoContenedor;
import utnfc.isi.back.contenedoresservice.exception.ReglaNegocioException;
import utnfc.isi.back.contenedoresservice.exception.ResourceNotFoundException;
import utnfc.isi.back.contenedoresservice.repository.EstadoContenedorRepository;

@Component
@AllArgsConstructor
public class ContenedorMapper {
    private final EstadoContenedorRepository estadoContenedorRepository;

    public ContenedorDTO toDTO(Contenedor entity) {
        if (entity == null) return null;

        ContenedorDTO dto = new ContenedorDTO();
        dto.setId(entity.getId());
        dto.setPeso(entity.getPeso());
        dto.setVolumen(entity.getVolumen());
        dto.setIdCliente(entity.getIdCliente());

        if (entity.getEstadoContenedor() != null) {
            dto.setEstado(entity.getEstadoContenedor().getNombre());
        } else {
            throw new ReglaNegocioException("El contenedor no tiene estado definido");
        }

        return dto;
    }

    public Contenedor toEntity(ContenedorDTO dto) {
        if (dto == null) return null;

        Contenedor entity = new Contenedor();
        entity.setId(dto.getId());
        entity.setPeso(dto.getPeso());
        entity.setVolumen(dto.getVolumen());
        entity.setIdCliente(dto.getIdCliente());
        entity.setEstadoContenedor(estadoContenedorRepository.findByNombre(dto.getEstado())
                .orElseThrow(() -> new ResourceNotFoundException("El contenedor no existe")));
        // El estado se completa en el servicio, no desde el DTO

        return entity;
    }
}
