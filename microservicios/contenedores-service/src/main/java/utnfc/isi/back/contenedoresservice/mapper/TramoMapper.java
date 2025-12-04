package utnfc.isi.back.contenedoresservice.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import utnfc.isi.back.contenedoresservice.client.CamionClient;
import utnfc.isi.back.contenedoresservice.client.DireccionClient;
import utnfc.isi.back.contenedoresservice.dto.CamionDTO;
import utnfc.isi.back.contenedoresservice.entity.Tramo;
import utnfc.isi.back.contenedoresservice.dto.TramoDTO;
import utnfc.isi.back.contenedoresservice.exception.GlobalExceptionHandler;
import utnfc.isi.back.contenedoresservice.exception.ResourceNotFoundException;


@Component
@RequiredArgsConstructor
public class TramoMapper {

    private final DireccionClient direccionClient;
    private final CamionClient camionClient;

    public TramoDTO toDTO(Tramo entity) {
        if (entity == null) return null;

        TramoDTO dto = new TramoDTO();
        dto.setId(entity.getId());
        dto.setRuta(entity.getRuta() != null ? entity.getRuta() : null);
        CamionDTO camion = camionClient.obtenerCamion(entity.getIdCamion()).block();
        if (camion == null) {
            throw new ResourceNotFoundException("Camión no encontrado");
        }
        dto.setCamion(camion);
        dto.setTipoTramo(entity.getTipoTramo() != null ? entity.getTipoTramo() : null);
        dto.setEstadoTramo(entity.getEstadoTramo() != null ? entity.getEstadoTramo().getNombre() : null);

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
        entity.setIdCamion(dto.getCamion().getId());
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

