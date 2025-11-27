package utnfc.isi.back.contenedoresservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.contenedoresservice.dto.HistorialEstadoDTO;
import utnfc.isi.back.contenedoresservice.dto.SolicitudDTO;
import utnfc.isi.back.contenedoresservice.dto.SolicitudDetalleDTO;
import utnfc.isi.back.contenedoresservice.entity.Solicitud;
import utnfc.isi.back.contenedoresservice.entity.SolicitudEstadoHistorial;
import utnfc.isi.back.contenedoresservice.mapper.SolicitudMapper;
import utnfc.isi.back.contenedoresservice.repository.SolicitudEstadoHistorialRepository;
import utnfc.isi.back.contenedoresservice.service.SolicitudService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;
    private final SolicitudEstadoHistorialRepository historialRepository;
    private final SolicitudMapper solicitudMapper;

    // ============ OPERADOR ============
    // Listado general
    @PreAuthorize("hasRole('operador')")
    @GetMapping
    public ResponseEntity<List<SolicitudDetalleDTO>> findAll() {
        List<SolicitudDetalleDTO> result = solicitudService.findAllDetalle();
        return ResponseEntity.ok(result);
    }

    // ============ CLIENTE ============
    // Registrar solicitud (cliente)
    @PreAuthorize("hasRole('cliente')")
    @PostMapping
    public ResponseEntity<SolicitudDTO> save(@Valid @RequestBody SolicitudDTO solicitudDto) {
        var solicitud = solicitudMapper.toEntity(solicitudDto);
        var createdEntity = solicitudService.save(solicitud);
        var createdDto = solicitudMapper.toDTO(createdEntity);
        return ResponseEntity
                .created(URI.create(("api/solicitudes") + createdDto.getId()))
                .body(createdDto);
    }

    // ============ OPERADOR ============
    // Borrar solicitud
    @PreAuthorize("hasRole('operador')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        solicitudService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ============ CLIENTE ============
    // Detalle ampliado (cliente)
    @PreAuthorize("hasRole('cliente')")
    @GetMapping("/{id}/detalle")
    public ResponseEntity<SolicitudDetalleDTO> getDetalle(@PathVariable Long id) {
        var detalle = solicitudService.buildDetalle(id);
        return ResponseEntity.ok(detalle);
    }

    // ============ OPERADOR ============
    // Calcular costos y tiempos
    @PreAuthorize("hasRole('operador')")
    @PostMapping("/{id}/calcular")
    public ResponseEntity<SolicitudDetalleDTO> calcular(@PathVariable Long id) {
        var detalle = solicitudService.calcular(id);
        return ResponseEntity.ok(detalle);
    }

    // ============ OPERADOR ============
    // Asignar camión a solicitud
    @PreAuthorize("hasRole('operador')")
    @PostMapping("/{id}/camion/{idCamion}")
    public ResponseEntity<SolicitudDetalleDTO> asignarCamion(
            @PathVariable Long id,
            @PathVariable Long idCamion) {
        var detalle = solicitudService.asignarCamion(id, idCamion);
        return ResponseEntity.ok(detalle);
    }

    // ============ CLIENTE ============
    // Seguimiento
    @PreAuthorize("hasRole('cliente')")
    @GetMapping("/{id}/seguimiento")
    public ResponseEntity<List<SolicitudEstadoHistorial>> obtenerSeguimiento(@PathVariable Long id) {
        var historial = historialRepository.findByIdSolicitudOrderByFechaRegistroAsc(id);
        return ResponseEntity.ok(historial);
    }

    // ============ OPERADOR ============
    // Historial completo
    @PreAuthorize("hasRole('operador')")
    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialEstadoDTO>> getHistorial(@PathVariable Long id) {
        var historial = solicitudService.obtenerHistorial(id);
        return ResponseEntity.ok(historial);
    }

    // ============ OPERADOR ============
    // Cambiar estado de solicitud
    @PreAuthorize("hasRole('operador')")
    @PutMapping("/{id}/estado/{nuevoEstadoId}")
    public ResponseEntity<SolicitudDetalleDTO> cambiarEstado(
            @PathVariable Long id,
            @PathVariable Long nuevoEstadoId) {
        var detalle = solicitudService.cambiarEstado(id, nuevoEstadoId);
        return ResponseEntity.ok(detalle);
    }
}

