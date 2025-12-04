package utnfc.isi.back.contenedoresservice.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import utnfc.isi.back.contenedoresservice.dto.HistorialEstadoDTO;
import utnfc.isi.back.contenedoresservice.entity.SolicitudEstadoHistorial;
import utnfc.isi.back.contenedoresservice.repository.EstadoSolicitudRepository;

@Component
@RequiredArgsConstructor
public class SolicitudEstadoHistorialMapper {

    private final EstadoSolicitudRepository estadoSolicitudRepository;

    public HistorialEstadoDTO toDTO(SolicitudEstadoHistorial entity) {

        var estado = estadoSolicitudRepository.findById(entity.getEstadoSolicitud().getId())
                .map(e -> e.getNombre())
                .orElse("Estado desconocido");

        HistorialEstadoDTO dto = new HistorialEstadoDTO();
        dto.setEstado(estado);
        dto.setFecha(entity.getFechaRegistro());
        return dto;
    }
}
