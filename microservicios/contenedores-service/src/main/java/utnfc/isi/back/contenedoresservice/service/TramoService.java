package utnfc.isi.back.contenedoresservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utnfc.isi.back.contenedoresservice.dto.TramoDTO;
import utnfc.isi.back.contenedoresservice.entity.EstadoTramo;
import utnfc.isi.back.contenedoresservice.entity.Tramo;
import utnfc.isi.back.contenedoresservice.exception.ReglaNegocioException;
import utnfc.isi.back.contenedoresservice.exception.ResourceNotFoundException;
import utnfc.isi.back.contenedoresservice.mapper.TramoMapper;
import utnfc.isi.back.contenedoresservice.repository.EstadoTramoRepository;
import utnfc.isi.back.contenedoresservice.repository.TramoRepository;
import utnfc.isi.back.contenedoresservice.exception.TransicionInvalidaException;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TramoService {

    private static final Logger log = LoggerFactory.getLogger(TramoService.class);
    private final TramoRepository tramoRepository;
    private final EstadoTramoRepository estadoTramoRepository;
    private final TramoMapper tramoMapper;

    // ============================
    // INICIO DE TRAMO
    // ============================
    public TramoDTO iniciarTramo(Long idTramo) {
        // Buscar el tramo en la base de datos
        Tramo tramo = tramoRepository.findById(idTramo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tramo con ID " + idTramo + " no encontrado"));

        // Verificar si el tramo ya tiene una fecha de inicio
        if (tramo.getFechaHoraInicio() != null) {
            log.warn("Intento de iniciar tramo con ID {} que ya tiene fecha de inicio: {}", idTramo, tramo.getFechaHoraInicio());
            throw new ReglaNegocioException("El tramo ya fue iniciado");
        }

        // Verificar si el estado del tramo es "Asignado" (ID 2)
        if (!tramo.getEstadoTramo().getId().equals(2L)) {
            log.warn("Intento de iniciar tramo con ID {} que no está en estado 'Asignado' (actual: {})",
                    idTramo, tramo.getEstadoTramo().getNombre());
            throw new TransicionInvalidaException(
                    tramo.getEstadoTramo().getNombre(),
                    "Iniciado",
                    List.of("Asignado")
            );
        }

        // Asignar el estado 'Iniciado' y la fecha de inicio
        EstadoTramo estadoIniciado = estadoTramoRepository.findById(2L)
                .orElseThrow(() -> new ResourceNotFoundException("Estado 'Iniciado' no encontrado"));

        tramo.setEstadoTramo(estadoIniciado);
        tramo.setFechaHoraInicio(LocalDateTime.now());

        // Guardar el tramo actualizado
        tramoRepository.save(tramo);

        // Log de operación principal
        log.info("Tramo con ID {} iniciado correctamente. Estado: {}, fechaHoraInicio: {}",
                idTramo, tramo.getEstadoTramo().getNombre(), tramo.getFechaHoraInicio());

        // Convertir la entidad Tramo a un DTO
        return tramoMapper.toDTO(tramo);
    }

    // ============================
    // FIN DE TRAMO
    // ============================
    public TramoDTO finalizarTramo(Long idTramo) {
        // Buscar el tramo
        Tramo tramo = tramoRepository.findById(idTramo)
                .orElseThrow(() -> new ReglaNegocioException("Tramo con ID " + idTramo + " no encontrado"));

        // Si el tramo no ha sido iniciado
        if (tramo.getFechaHoraInicio() == null) {
            log.warn("Intento de finalizar tramo con ID {} que no ha sido iniciado", idTramo);
            throw new ReglaNegocioException("El tramo no ha sido iniciado.");
        }

        // Si el tramo ya está finalizado
        if (tramo.getFechaHoraFin() != null) {
            log.warn("Intento de finalizar tramo con ID {} que ya fue finalizado en {}", idTramo, tramo.getFechaHoraFin());
            throw new ReglaNegocioException("El tramo ya está finalizado.");
        }

        // Setear la fecha de fin
        tramo.setFechaHoraFin(LocalDateTime.now());

        // Guardar el tramo
        tramoRepository.save(tramo);

        // Log de operación principal
        log.info("Tramo con ID {} finalizado correctamente. FechaHoraInicio: {}, FechaHoraFin: {}",
                idTramo, tramo.getFechaHoraInicio(), tramo.getFechaHoraFin());

        return tramoMapper.toDTO(tramo);
    }
}

