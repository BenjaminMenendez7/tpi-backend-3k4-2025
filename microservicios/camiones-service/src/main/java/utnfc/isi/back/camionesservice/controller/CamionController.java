package utnfc.isi.back.camionesservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.camionesservice.dto.CamionConTransportistaDTO;
import utnfc.isi.back.camionesservice.dto.CamionDTO;
import utnfc.isi.back.camionesservice.dto.CamionRequestDTO;
import utnfc.isi.back.camionesservice.dto.TarifaDTO;
import utnfc.isi.back.camionesservice.entity.Camion;
import utnfc.isi.back.camionesservice.service.CamionService;
import utnfc.isi.back.common.exceptions.ResourceNotFoundException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/camiones")
public class CamionController {

    private final CamionService camionService;

    @PreAuthorize("hasRole('OPERADOR')")
    @GetMapping
    public List<CamionDTO> listar() {
        return camionService.obtenerTodos();
    }

    @PreAuthorize("hasAnyRole('OPERADOR','TRANSPORTISTA')")
    @GetMapping("/{id}")
    public CamionDTO obtenerPorId(@PathVariable Long id) {
        return camionService.obtenerPorId(id);
    }

    @PreAuthorize("hasAnyRole('OPERADOR','CLIENTE')")
    @GetMapping("/patente/{patente}")
    public CamionDTO obtenerPorPatente(@PathVariable String patente) {
        return camionService.obtenerPorPatente(patente);
    }

    @PreAuthorize("hasRole('OPERADOR')")
    @GetMapping("/disponibles")
    public List<CamionDTO> obtenerDisponibles() {
        return camionService.obtenerDisponibles();
    }

    @PreAuthorize("hasRole('OPERADOR')")
    @GetMapping("/transportista/{idTransportista}")
    public List<CamionDTO> obtenerPorTransportista(@PathVariable Long idTransportista) {
        return camionService.obtenerPorTransportista(idTransportista);
    }

    @PreAuthorize("hasRole('OPERADOR')")
    @GetMapping("/{idCamion}/detalle")
    public CamionConTransportistaDTO obtenerDetalleConTransportista(@PathVariable Long idCamion) {
        return camionService.obtenerDetalleConTransportista(idCamion);
    }

    // ===========================
    //   ACCIONES DEL OPERADOR
    // ===========================

    @PreAuthorize("hasRole('OPERADOR')")
    @PostMapping
    public CamionDTO crear(@RequestBody CamionRequestDTO dto) {
        return camionService.crearCamion(dto);
    }

    @PreAuthorize("hasRole('OPERADOR')")
    @PatchMapping("/{id}")
    public CamionDTO actualizar(@PathVariable Long id, @RequestBody CamionRequestDTO dto) {
        return camionService.actualizarCamion(id, dto);
    }

    @PreAuthorize("hasRole('OPERADOR')")
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        camionService.eliminarCamion(id);
    }

    @PreAuthorize("hasRole('OPERADOR')")
    @PatchMapping("/{idCamion}/disponibilidad/{disponible}")
    public CamionDTO cambiarDisponibilidad(@PathVariable Long idCamion, @PathVariable boolean disponible) {
        return camionService.cambiarDisponibilidad(idCamion, disponible);
    }

    @PreAuthorize("hasRole('OPERADOR')")
    @PutMapping("/{id}/ocupar")
    public void marcarNoDisponible(@PathVariable Long id) {
        camionService.cambiarDisponibilidad(id, false);
    }

    @PreAuthorize("hasRole('OPERADOR')")
    @PutMapping("/{id}/liberar")
    public void marcarDisponible(@PathVariable Long id) {
        camionService.cambiarDisponibilidad(id, true);
    }

}
