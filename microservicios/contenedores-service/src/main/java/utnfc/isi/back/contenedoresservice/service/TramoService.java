package utnfc.isi.back.contenedoresservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utnfc.isi.back.contenedoresservice.dto.TramoDTO;
import utnfc.isi.back.contenedoresservice.entity.EstadoTramo;
import utnfc.isi.back.contenedoresservice.entity.Tramo;
import utnfc.isi.back.contenedoresservice.exception.ReglaNegocioException;
import utnfc.isi.back.contenedoresservice.exception.ResourceNotFoundException;
import utnfc.isi.back.contenedoresservice.mapper.TramoMapper;
import utnfc.isi.back.contenedoresservice.repository.EstadoTramoRepository;
import utnfc.isi.back.contenedoresservice.repository.TramoRepository;
import utnfc.isi.back.contenedoresservice.exception.TransicionInvalidaException;
import utnfc.isi.back.contenedoresservice.service.enums.EstadoTramoEnum;


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
    @Transactional
    public TramoDTO iniciarTramo(Long idTramo) {
        // Buscar el tramo en la base de datos
        Tramo tramo = obtenerTramo(idTramo);

        validarInicio(tramo);

        EstadoTramo estadoIniciado = estadoTramoRepository.findByNombre(EstadoTramoEnum.INICIADO.name())
                .orElseThrow(() -> new  ResourceNotFoundException("Estado 'Iniciado' no encontrado"));

        tramo.setEstadoTramo(estadoIniciado);
        tramo.setFechaHoraInicio(LocalDateTime.now());

        tramoRepository.save(tramo);

        log.info("Tramo {} iniciado. Estado: {}, Inicio: {}", tramo.getId(), tramo.getEstadoTramo().getNombre(), tramo.getFechaHoraInicio());
        return tramoMapper.toDTO(tramo);
    }

    // ============================
    // FIN DE TRAMO
    // ============================
    @Transactional
    public TramoDTO finalizarTramo(Long idTramo) {
        // Buscar el tramo
        Tramo tramo = obtenerTramo(idTramo);

        validarFin(tramo);

        EstadoTramo estadoFinalizado = estadoTramoRepository.findByNombre(EstadoTramoEnum.FINALIZADO.name())
                .orElseThrow(() -> new  ResourceNotFoundException("Estado 'Finalizado' no encontrado"));

        // Setear la fecha de fin
        tramo.setEstadoTramo(estadoFinalizado);
        tramo.setFechaHoraFin(LocalDateTime.now());

        tramoRepository.save(tramo);

        // Log de operación principal
        log.info("Tramo con ID {} finalizado correctamente. FechaHoraInicio: {}, FechaHoraFin: {}",
                idTramo, tramo.getFechaHoraInicio(), tramo.getFechaHoraFin());

        return tramoMapper.toDTO(tramo);
    }


    // ===========================
    // MÉTODOS PRIVADOS
    // ===========================
    private Tramo obtenerTramo(Long idTramo) {
        return tramoRepository.findById(idTramo)
                .orElseThrow(() -> new ResourceNotFoundException("Tramo con ID " + idTramo + " no encontrado"));
    }

    private void validarInicio(Tramo tramo) {
        if (tramo.getFechaHoraInicio() != null) {
            throw new ReglaNegocioException("El tramo ya fue iniciado");
        }
        if (!"Asignado".equalsIgnoreCase(tramo.getEstadoTramo().getNombre())) {
            throw new TransicionInvalidaException(tramo.getEstadoTramo().getNombre(), "Iniciado", List.of("Asignado"));
        }
    }

    private void validarFin(Tramo tramo) {
        if (tramo.getFechaHoraInicio() == null) {
            throw new ReglaNegocioException("El tramo no ha sido iniciado");
        }
        if (tramo.getFechaHoraFin() != null) {
            throw new ReglaNegocioException("El tramo ya está finalizado");
        }
    }
}

