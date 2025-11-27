package utnfc.isi.back.contenedoresservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utnfc.isi.back.contenedoresservice.entity.SolicitudEstadoHistorial;
import utnfc.isi.back.contenedoresservice.repository.SolicitudEstadoHistorialRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SolicitudEstadoHistorialService {

    private final SolicitudEstadoHistorialRepository historialRepository;

    /**
     * Guarda un nuevo registro en el historial de estado para una solicitud.
     * @param idSolicitud ID de la solicitud
     * @param idEstadoSolicitud ID del estado (FK a estado_solicitud)
     */
    public void registrar(Long idSolicitud, Long idEstadoSolicitud) {
        SolicitudEstadoHistorial historial = new SolicitudEstadoHistorial();
        historial.setIdSolicitud(idSolicitud);
        historial.setIdEstadoSolicitud(idEstadoSolicitud);
        historial.setFechaRegistro(LocalDateTime.now());
        historialRepository.save(historial);
    }
}
