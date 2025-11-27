package utnfc.isi.back.contenedoresservice.mapper;

import org.springframework.stereotype.Component;
import utnfc.isi.back.contenedoresservice.entity.Contenedor;
import utnfc.isi.back.contenedoresservice.dto.ContenedorDTO;

@Component
public class ContenedorMapper {

    public ContenedorDTO toDTO(Contenedor entity) {
        if (entity == null) return null;

        ContenedorDTO dto = new ContenedorDTO();
        dto.setId(entity.getId());
        dto.setPeso(entity.getPeso());
        dto.setVolumen(entity.getVolumen());
        dto.setIdCliente(entity.getIdCliente());

        if (entity.getEstadoContenedor() != null) {
            dto.setEstado(entity.getEstadoContenedor().getNombre());
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

        // El estado se completa en el servicio, no desde el DTO
        entity.setEstadoContenedor(null);

        return entity;
    }
}
