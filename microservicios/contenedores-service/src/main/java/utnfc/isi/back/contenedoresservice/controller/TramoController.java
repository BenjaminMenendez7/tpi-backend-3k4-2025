package utnfc.isi.back.contenedoresservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.server.reactive.ServerHttpRequest;

import reactor.core.publisher.Mono;
import utnfc.isi.back.contenedoresservice.dto.TramoDTO;
import utnfc.isi.back.contenedoresservice.service.TramoService;

import java.net.URI;

@RestController
@RequestMapping("/tramos")
@RequiredArgsConstructor
public class TramoController {

    private final TramoService tramoService;

    // TRANSPORTISTA: iniciar tramo
    @PreAuthorize("hasRole('TRANSPORTISTA')")
    @PostMapping("/{idTramo}/inicio")
    public Mono<ResponseEntity<TramoDTO>> iniciarTramo(@PathVariable Long idTramo, ServerHttpRequest request) {
        TramoDTO result = tramoService.iniciarTramo(idTramo);
        URI location = request.getURI().resolve(result.getId().toString());
        return Mono.just(ResponseEntity.ok().location(location).body(result));
    }

    // TRANSPORTISTA: finalizar tramo
    @PreAuthorize("hasRole('TRANSPORTISTA')")
    @PostMapping("/{idTramo}/fin")
    public Mono<ResponseEntity<TramoDTO>> finalizarTramo(@PathVariable Long idTramo, ServerHttpRequest request) {
        TramoDTO result = tramoService.finalizarTramo(idTramo);
        URI location = request.getURI().resolve(result.getId().toString());
        return Mono.just(ResponseEntity.ok().location(location).body(result));
    }
}
