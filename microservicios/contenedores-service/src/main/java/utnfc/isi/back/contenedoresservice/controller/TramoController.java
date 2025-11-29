package utnfc.isi.back.contenedoresservice.controller;

import jakarta.servlet.Servlet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import utnfc.isi.back.contenedoresservice.dto.TramoDTO;
import utnfc.isi.back.contenedoresservice.entity.Tramo;
import utnfc.isi.back.contenedoresservice.service.TramoService;

import java.net.URI;

@RestController
@RequestMapping("/api/tramos")
@RequiredArgsConstructor
public class TramoController {

    private final TramoService tramoService;

    // TRANSPORTISTA: iniciar tramo
    @PreAuthorize("hasRole('transportista')")
    @PostMapping("/{idTramo}/inicio")
    public ResponseEntity<TramoDTO> iniciarTramo(@PathVariable Long idTramo) {
        TramoDTO result = tramoService.iniciarTramo(idTramo);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.ok().location(location).body(result);
    }

    // TRANSPORTISTA: finalizar tramo
    @PreAuthorize("hasRole('transportista')")
    @PostMapping("/{idTramo}/fin")
    public ResponseEntity<TramoDTO> finalizarTramo(@PathVariable Long idTramo) {
        TramoDTO result = tramoService.finalizarTramo(idTramo);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.ok().location(location).body(result);
    }
}
