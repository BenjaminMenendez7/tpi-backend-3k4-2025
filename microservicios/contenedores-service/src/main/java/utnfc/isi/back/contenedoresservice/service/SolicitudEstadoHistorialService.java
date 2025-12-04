package utnfc.isi.back.contenedoresservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utnfc.isi.back.contenedoresservice.entity.EstadoSolicitud;
import utnfc.isi.back.contenedoresservice.entity.SolicitudEstadoHistorial;
import utnfc.isi.back.contenedoresservice.repository.EstadoSolicitudRepository;
import utnfc.isi.back.contenedoresservice.repository.SolicitudEstadoHistorialRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SolicitudEstadoHistorialService {

    private final SolicitudEstadoHistorialRepository historialRepository;
    private final EstadoSolicitudRepository estadoSolicitudRepository;

    @Transactional
    public void registrar(Long idSolicitud, Long idEstadoSolicitud) {
        EstadoSolicitud estado = estadoSolicitudRepository.findById(idEstadoSolicitud)
                .orElseThrow(() -> new IllegalArgumentException("Estado no existe: " + idEstadoSolicitud));

        SolicitudEstadoHistorial historial = new SolicitudEstadoHistorial();
        historial.setIdSolicitud(idSolicitud);
        historial.setEstadoSolicitud(estado);          // ✅ setear relación
        historial.setFechaRegistro(LocalDateTime.now());

        historialRepository.save(historial);
    }

}
