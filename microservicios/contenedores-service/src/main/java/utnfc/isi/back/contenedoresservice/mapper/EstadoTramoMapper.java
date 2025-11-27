package utnfc.isi.back.contenedoresservice.mapper;

import org.springframework.stereotype.Component;
import utnfc.isi.back.contenedoresservice.entity.EstadoTramo;
import utnfc.isi.back.contenedoresservice.dto.EstadoTramoDTO;

@Component
public class EstadoTramoMapper {

    public EstadoTramoDTO toDTO(EstadoTramo entity) {
        if (entity == null) return null;

        EstadoTramoDTO dto = new EstadoTramoDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());

        return dto;
    }

    public EstadoTramo toEntity(EstadoTramoDTO dto) {
        if (dto == null) return null;

        EstadoTramo entity = new EstadoTramo();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());

        return entity;
    }
}
