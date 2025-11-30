package utnfc.isi.back.camionesservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.camionesservice.dto.CamionConTransportistaDTO;
import utnfc.isi.back.camionesservice.dto.CamionDTO;
import utnfc.isi.back.camionesservice.dto.CamionRequestDTO;
import utnfc.isi.back.camionesservice.entity.Camion;
import utnfc.isi.back.camionesservice.repository.CamionRepository;
import utnfc.isi.back.camionesservice.service.CamionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/camiones")
public class CamionController {

    private final CamionService camionService;
    private final CamionRepository camionRepository;

    // ===========================
    //           CONSULTAS
    // ===========================

    // LIBRE - Listar camiones
    @PreAuthorize("hasRole('OPERADOR')")
    @GetMapping
    public List<CamionDTO> listar() {
               return camionService.obtenerTodos();
          }

    // LIBRE - Obtener camión por ID
    @PreAuthorize("hasAnyRole('OPERADOR','TRANSPORTISTA')")
    @GetMapping("/{id}")
    public CamionDTO obtenerPorId(@PathVariable Long id) {
        return camionService.obtenerPorId(id);
    }

    // LIBRE - Buscar por patente
    @PreAuthorize("hasAnyRole('OPERADOR','CLIENTE')")
    @GetMapping("/patente/{patente}")
    public CamionDTO obtenerPorPatente(@PathVariable String patente) {
        return camionService.obtenerPorPatente(patente);
    }

    // LIBRE - Camiones disponibles
    @PreAuthorize("hasRole('OPERADOR')")
    @GetMapping("/disponibles")
    public List<CamionDTO> obtenerDisponibles() {
        return camionService.obtenerDisponibles();
    }

    // LIBRE - Camiones por transportista
    @PreAuthorize("hasRole('OPERADOR')")
    @GetMapping("/transportista/{idTransportista}")
    public List<CamionDTO> obtenerPorTransportista(@PathVariable Long idTransportista) {
        return camionService.obtenerPorTransportista(idTransportista);
    }

    // LIBRE - Detalle completo
    @PreAuthorize("hasRole('OPERADOR')")
    @GetMapping("/{idCamion}/detalle")
    public CamionConTransportistaDTO obtenerDetalleConTransportista(@PathVariable Long idCamion) {
        return camionService.obtenerDetalleConTransportista(idCamion);
    }


    // ===========================
    //   ACCIONES DEL OPERADOR
    // ===========================

    // CREAR CAMIÓN
    @PreAuthorize("hasRole('OPERADOR')")
    @PostMapping
    public CamionDTO crear(@RequestBody CamionRequestDTO dto) {
        return camionService.crearCamion(dto);
    }

    // ACTUALIZAR CAMIÓN
    @PreAuthorize("hasRole('OPERADOR')")
    @PutMapping("/{id}")
    public CamionDTO actualizar(@PathVariable Long id, @RequestBody CamionRequestDTO dto) {
        return camionService.actualizarCamion(id, dto);
    }

    // ELIMINAR CAMIÓN
    @PreAuthorize("hasRole('OPERADOR')")
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        camionService.eliminarCamion(id);
    }

    // CAMBIAR DISPONIBILIDAD
    @PreAuthorize("hasRole('OPERADOR')")
    @PatchMapping("/{idCamion}/disponibilidad/{disponible}")
    public CamionDTO cambiarDisponibilidad(@PathVariable Long idCamion, @PathVariable boolean disponible) {
        return camionService.cambiarDisponibilidad(idCamion, disponible);
    }

    // MARCAR NO DISPONIBLE
    @PreAuthorize("hasRole('OPERADOR')")
    @PutMapping("/{id}/ocupar")
    public void marcarNoDisponible(@PathVariable Long id) {
        Camion camion = camionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Camión no encontrado"));
        camion.setDisponible(false);
        camionRepository.save(camion);
    }

    // MARCAR DISPONIBLE
    @PreAuthorize("hasRole('OPERADOR')")
    @PutMapping("/{id}/liberar")
    public void marcarDisponible(@PathVariable Long id) {
        Camion camion = camionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Camión no encontrado"));
        camion.setDisponible(true);
        camionRepository.save(camion);
    }
}

