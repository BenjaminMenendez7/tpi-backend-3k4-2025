package utnfc.isi.back.contenedoresservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.contenedoresservice.dto.HistorialEstadoDTO;
import utnfc.isi.back.contenedoresservice.dto.SolicitudDetalleDTO;
import utnfc.isi.back.contenedoresservice.entity.Solicitud;
import utnfc.isi.back.contenedoresservice.entity.SolicitudEstadoHistorial;
import utnfc.isi.back.contenedoresservice.repository.SolicitudEstadoHistorialRepository;
import utnfc.isi.back.contenedoresservice.service.SolicitudService;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;
    private final SolicitudEstadoHistorialRepository historialRepository;

    // ============ OPERADOR ============
    // Listado general
    @PreAuthorize("hasRole('operador')")
    @GetMapping
    public List<SolicitudDetalleDTO> findAll() {
        return solicitudService.findAllDetalle();
    }

    // ============ CLIENTE ============
    // Registrar solicitud (cliente)
    @PreAuthorize("hasRole('cliente')")
    @PostMapping
    public Solicitud save(@RequestBody Solicitud solicitud) {
        return solicitudService.save(solicitud);
    }

    // ============ OPERADOR ============
    // Borrar solicitud
    @PreAuthorize("hasRole('operador')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        solicitudService.delete(id);
    }

    // ============ CLIENTE ============
    // Detalle ampliado (cliente)
    @PreAuthorize("hasRole('cliente')")
    @GetMapping("/{id}/detalle")
    public SolicitudDetalleDTO getDetalle(@PathVariable Long id) {
        return solicitudService.buildDetalle(id);
    }

    // ============ OPERADOR ============
    // Calcular costos y tiempos
    @PreAuthorize("hasRole('operador')")
    @PostMapping("/{id}/calcular")
    public SolicitudDetalleDTO calcular(@PathVariable Long id) {
        return solicitudService.calcular(id);
    }

    // ============ OPERADOR ============
    // Asignar camión a solicitud
    @PreAuthorize("hasRole('operador')")
    @PostMapping("/{id}/asignar-camion/{idCamion}")
    public SolicitudDetalleDTO asignarCamion(
            @PathVariable Long id,
            @PathVariable Long idCamion) {
        return solicitudService.asignarCamion(id, idCamion);
    }

    // ============ CLIENTE ============
    // Seguimiento
    @PreAuthorize("hasRole('cliente')")
    @GetMapping("/{id}/seguimiento")
    public List<SolicitudEstadoHistorial> obtenerSeguimiento(@PathVariable Long id) {
        return historialRepository.findByIdSolicitudOrderByFechaRegistroAsc(id);
    }

    // ============ OPERADOR ============
    // Historial completo
    @PreAuthorize("hasRole('operador')")
    @GetMapping("/{id}/historial")
    public List<HistorialEstadoDTO> getHistorial(@PathVariable Long id) {
        return solicitudService.obtenerHistorial(id);
    }

    // ============ OPERADOR ============
    // Cambiar estado de solicitud
    @PreAuthorize("hasRole('operador')")
    @PostMapping("/{id}/estado/{nuevoEstadoId}")
    public SolicitudDetalleDTO cambiarEstado(
            @PathVariable Long id,
            @PathVariable Long nuevoEstadoId) {
        return solicitudService.cambiarEstado(id, nuevoEstadoId);
    }

}

