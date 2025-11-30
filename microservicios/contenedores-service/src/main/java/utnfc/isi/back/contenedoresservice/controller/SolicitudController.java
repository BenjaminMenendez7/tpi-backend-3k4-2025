package utnfc.isi.back.contenedoresservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.server.reactive.ServerHttpRequest;

import utnfc.isi.back.contenedoresservice.dto.HistorialEstadoDTO;
import utnfc.isi.back.contenedoresservice.dto.SolicitudDTO;
import utnfc.isi.back.contenedoresservice.dto.SolicitudDetalleDTO;
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
    @PreAuthorize("hasRole('OPERADOR')")
    @GetMapping
    public ResponseEntity<List<SolicitudDetalleDTO>> findAll() {
        List<SolicitudDetalleDTO> result = solicitudService.findAllDetalle();
        return ResponseEntity.ok(result);
    }

    // ============ CLIENTE ============
    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping
    // ✅ WebFlux: Recibe ServerHttpRequest
    public ResponseEntity<SolicitudDetalleDTO> save(@Valid @RequestBody SolicitudDTO solicitudDto, ServerHttpRequest request) {
        SolicitudDetalleDTO result = solicitudService.save(solicitudDto);

        // ✅ Lógica WebFlux para URI
        URI location = request.getURI().resolve(result.getId().toString());

        return ResponseEntity.created(location).body(result);
    }

    // ============ OPERADOR ============
    @PreAuthorize("hasRole('OPERADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        solicitudService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ============ CLIENTE ============
    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/{id}/detalle")
    public ResponseEntity<SolicitudDetalleDTO> getDetalle(@PathVariable Long id) {
        var detalle = solicitudService.buildDetalle(id);
        return ResponseEntity.ok(detalle);
    }

    // ============ OPERADOR ============
    @PreAuthorize("hasRole('OPERADOR')")
    @PostMapping("/{id}/calcular")
    public ResponseEntity<SolicitudDetalleDTO> calcular(@PathVariable Long id) {
        var detalle = solicitudService.calcular(id);
        return ResponseEntity.ok(detalle);
    }

    // ============ OPERADOR ============
    @PreAuthorize("hasRole('OPERADOR')")
    @PostMapping("/{id}/camion/{idCamion}")
    public ResponseEntity<SolicitudDetalleDTO> asignarCamion(
            @PathVariable Long id,
            @PathVariable Long idCamion) {
        var detalle = solicitudService.asignarCamion(id, idCamion);
        return ResponseEntity.ok(detalle);
    }

    // ============ CLIENTE ============
    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/{id}/seguimiento")
    public ResponseEntity<List<SolicitudEstadoHistorial>> obtenerSeguimiento(@PathVariable Long id) {
        var historial = historialRepository.findByIdSolicitudOrderByFechaRegistroAsc(id);
        return ResponseEntity.ok(historial);
    }

    // ============ OPERADOR ============
    @PreAuthorize("hasRole('OPERADOR')")
    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialEstadoDTO>> getHistorial(@PathVariable Long id) {
        var historial = solicitudService.obtenerHistorial(id);
        return ResponseEntity.ok(historial);
    }

    // ============ OPERADOR ============
    @PreAuthorize("hasRole('OPERADOR')")
    @PutMapping("/{id}/estado/{nuevoEstadoId}")
    public ResponseEntity<SolicitudDetalleDTO> cambiarEstado(
            @PathVariable Long id,
            @PathVariable Long nuevoEstadoId) {
        var detalle = solicitudService.cambiarEstado(id, nuevoEstadoId);
        return ResponseEntity.ok(detalle);
    }
}