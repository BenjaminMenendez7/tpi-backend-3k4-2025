package utnfc.isi.back.contenedoresservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.server.reactive.ServerHttpRequest;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
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
@RequestMapping("/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;
    private final SolicitudEstadoHistorialRepository historialRepository;
    private final SolicitudMapper solicitudMapper;

    // ============ OPERADOR ============
    @PreAuthorize("hasRole('OPERADOR')")
    @GetMapping
    public Mono<ResponseEntity<List<SolicitudDetalleDTO>>> findAll() {
        List<SolicitudDetalleDTO> result = solicitudService.findAllDetalle();
        return Mono.just(ResponseEntity.ok(result));
    }

    // ============ CLIENTE ============
    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping
    public Mono<ResponseEntity<SolicitudDetalleDTO>> save(
            @Valid @RequestBody SolicitudDTO solicitudDto,
            ServerHttpRequest request) {
        return Mono.fromCallable(() -> solicitudService.save(solicitudDto))
                .subscribeOn(Schedulers.boundedElastic())
                .map(result -> {
                    URI location = request.getURI().resolve(result.getId().toString());
                    return ResponseEntity.created(location).body(result);
                });
    }

    // ============ OPERADOR ============
    @PreAuthorize("hasRole('OPERADOR')")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        solicitudService.delete(id);
        return Mono.just(ResponseEntity.noContent().build());
    }

    // ============ CLIENTE ============
    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/{id}/detalle")
    public Mono<ResponseEntity<SolicitudDetalleDTO>> getDetalle(@PathVariable Long id) {
        SolicitudDetalleDTO detalle = solicitudService.buildDetalle(id);
        return Mono.just(ResponseEntity.ok(detalle));
    }

    // ============ OPERADOR ============
    @PreAuthorize("hasRole('OPERADOR')")
    @PostMapping("/{id}/calcular")
    public Mono<ResponseEntity<SolicitudDetalleDTO>> calcular(@PathVariable Long id) {
        SolicitudDetalleDTO detalle = solicitudService.calcular(id);
        return Mono.just(ResponseEntity.ok(detalle));
    }

    @PreAuthorize("hasRole('OPERADOR')")
    @PostMapping("/{id}/camion/{idCamion}")
    public Mono<ResponseEntity<SolicitudDetalleDTO>> asignarCamion(
            @PathVariable Long id,
            @PathVariable Long idCamion) {
        SolicitudDetalleDTO detalle = solicitudService.asignarCamion(id, idCamion);
        return Mono.just(ResponseEntity.ok(detalle));
    }

    // ============ CLIENTE ============
    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/{id}/seguimiento")
    public Mono<ResponseEntity<List<SolicitudEstadoHistorial>>> obtenerSeguimiento(@PathVariable Long id) {
        List<SolicitudEstadoHistorial> historial = historialRepository.findByIdSolicitudOrderByFechaRegistroAsc(id);
        return Mono.just(ResponseEntity.ok(historial));
    }

    // ============ OPERADOR ============
    @PreAuthorize("hasRole('OPERADOR')")
    @GetMapping("/{id}/historial")
    public Mono<ResponseEntity<List<HistorialEstadoDTO>>> getHistorial(@PathVariable Long id) {
        List<HistorialEstadoDTO> historial = solicitudService.obtenerHistorial(id);
        return Mono.just(ResponseEntity.ok(historial));
    }

    @PreAuthorize("hasRole('OPERADOR')")
    @PutMapping("/{id}/estado/{nuevoEstadoId}")
    public Mono<ResponseEntity<SolicitudDetalleDTO>> cambiarEstado(
            @PathVariable Long id,
            @PathVariable Long nuevoEstadoId) {
        SolicitudDetalleDTO detalle = solicitudService.cambiarEstado(id, nuevoEstadoId);
        return Mono.just(ResponseEntity.ok(detalle));
    }
}