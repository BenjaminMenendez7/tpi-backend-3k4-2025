package utnfc.isi.back.contenedoresservice.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import utnfc.isi.back.contenedoresservice.client.DireccionClient;
import utnfc.isi.back.contenedoresservice.entity.Tramo;
import utnfc.isi.back.contenedoresservice.dto.TramoDTO;


@Component
@RequiredArgsConstructor
public class TramoMapper {

    private final DireccionClient direccionClient;

    public TramoDTO toDTO(Tramo entity) {
        if (entity == null) return null;

        TramoDTO dto = new TramoDTO();
        dto.setId(entity.getId());
        dto.setIdRuta(entity.getRuta() != null ? entity.getRuta().getId() : null);
        dto.setIdCamion(entity.getIdCamion());
        dto.setIdTipoTramo(entity.getTipoTramo() != null ? entity.getTipoTramo().getId() : null);
        dto.setIdEstadoTramo(entity.getEstadoTramo() != null ? entity.getEstadoTramo().getId() : null);

        dto.setOrigen(entity.getOrigen());
        dto.setDestino(entity.getDestino());

        dto.setFechaHoraInicio(entity.getFechaHoraInicio());
        dto.setFechaHoraFin(entity.getFechaHoraFin());

        dto.setCostoAproximado(entity.getCostoAproximado());
        dto.setCostoReal(entity.getCostoReal());

        dto.setDistanciaKm(entity.getDistanciaKm());
        dto.setTiempoEstimadoMinutos(entity.getTiempoEstimadoMinutos());

        // ============================
        // AGREGADO PARA NOMBRES ORIGEN/DESTINO
        // ============================
        var origenMap = direccionClient.obtenerDireccion(entity.getOrigen());
        var destinoMap = direccionClient.obtenerDireccion(entity.getDestino());

        dto.setOrigenNombre(
                origenMap != null && origenMap.get("nombreCalle") != null
                        ? origenMap.get("nombreCalle").toString()
                        : null
        );

        dto.setDestinoNombre(
                destinoMap != null && destinoMap.get("nombreCalle") != null
                        ? destinoMap.get("nombreCalle").toString()
                        : null
        );


        return dto;
    }



    public Tramo toEntity(TramoDTO dto) {
        if (dto == null) return null;

        Tramo entity = new Tramo();
        entity.setId(dto.getId());
        entity.setIdCamion(dto.getIdCamion());
        entity.setOrigen(dto.getOrigen());
        entity.setDestino(dto.getDestino());
        entity.setFechaHoraInicio(dto.getFechaHoraInicio());
        entity.setFechaHoraFin(dto.getFechaHoraFin());
        entity.setCostoAproximado(dto.getCostoAproximado());
        entity.setCostoReal(dto.getCostoReal());

        // FK se setean en servicio
        entity.setRuta(null);
        entity.setTipoTramo(null);
        entity.setEstadoTramo(null);

        return entity;
    }
}

