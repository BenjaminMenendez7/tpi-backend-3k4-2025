package utnfc.isi.back.contenedoresservice.mapper;

import org.springframework.stereotype.Component;
import utnfc.isi.back.contenedoresservice.dto.ContenedorDTO;
import utnfc.isi.back.contenedoresservice.dto.SolicitudDetalleDTO;
import utnfc.isi.back.contenedoresservice.entity.Solicitud;
import utnfc.isi.back.contenedoresservice.dto.SolicitudDTO;

@Component
public class SolicitudMapper {
    public SolicitudDetalleDTO toDTO(Solicitud entity) {
        if (entity == null) return null;

        SolicitudDetalleDTO dto = new SolicitudDetalleDTO();
        dto.setId(entity.getId());

        // entidad.fechaCreacion → dto.fechaSolicitud
        dto.setFechaSolicitud(entity.getFechaCreacion());

        // FK contenedor
        dto.setContenedor(entity.getContenedor() != null ? entity.getContenedor() : null);

        // La entidad NO tiene depósito → null
        dto.setDeposito(null);

        // La entidad todavía NO tiene ruta → null
        dto.setRuta(null);

        // EstadoSolicitud → String estado (el DTO pide un string)
        dto.setEstadoSolicitud(
                entity.getEstadoSolicitud() != null ? entity.getEstadoSolicitud().getNombre() : null
        );

        // costoTotal: usás costoFinal si existe, si no, costoEstimado
        if (entity.getCostoFinal() != null) {
            dto.setCostoTotal(entity.getCostoFinal());
        } else {
            dto.setCostoTotal(entity.getCostoEstimado());
        }

        // tiempo estimado: tu entidad NO tiene tiempoEstimado → null
        dto.setTiempoEstimadoMinutos(null);

        return dto;
    }

    public Solicitud toEntity(SolicitudDTO dto) {
        if (dto == null) return null;

        Solicitud entity = new Solicitud();

        // dto.fechaSolicitud → entidad.fechaCreacion
        entity.setFechaCreacion(null);

        // La entidad necesita idCliente, pero el DTO no lo trae
        entity.setIdCliente(null); // lo vas a completar al crear la solicitud

        // contenedor: el DTO trae solo el id → no instanciar acá
        entity.setContenedor(null);

        // estadoSolicitud: el DTO trae un string → no mapear acá
        entity.setEstadoSolicitud(null);

        // costos y tiempos: el DTO trae uno solo (costoTotal)
        entity.setCostoEstimado(null);
        entity.setCostoFinal(null);
        entity.setTiempoReal(null);

        return entity;
    }
}
