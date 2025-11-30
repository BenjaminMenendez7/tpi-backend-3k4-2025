package utnfc.isi.back.contenedoresservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.server.reactive.ServerHttpRequest;

import utnfc.isi.back.contenedoresservice.dto.TramoDTO;
import utnfc.isi.back.contenedoresservice.service.TramoService;

import java.net.URI;

@RestController
@RequestMapping("/api/tramos")
@RequiredArgsConstructor
public class TramoController {

    private final TramoService tramoService;

    // TRANSPORTISTA: iniciar tramo
    @PreAuthorize("hasRole('TRANSPORTISTA')") // ✅ Rol en mayúsculas
    @PostMapping("/{idTramo}/inicio")
    // ✅ WebFlux: Recibe ServerHttpRequest
    public ResponseEntity<TramoDTO> iniciarTramo(@PathVariable Long idTramo, ServerHttpRequest request) {
        TramoDTO result = tramoService.iniciarTramo(idTramo);

        // ✅ Lógica WebFlux para URI
        URI location = request.getURI().resolve(result.getId().toString());

        return ResponseEntity.ok().location(location).body(result);
    }

    // TRANSPORTISTA: finalizar tramo
    @PreAuthorize("hasRole('TRANSPORTISTA')") // ✅ Rol en mayúsculas
    @PostMapping("/{idTramo}/fin")
    // ✅ WebFlux: Recibe ServerHttpRequest
    public ResponseEntity<TramoDTO> finalizarTramo(@PathVariable Long idTramo, ServerHttpRequest request) {
        TramoDTO result = tramoService.finalizarTramo(idTramo);

        // ✅ Lógica WebFlux para URI
        URI location = request.getURI().resolve(result.getId().toString());

        return ResponseEntity.ok().location(location).body(result);
    }
}