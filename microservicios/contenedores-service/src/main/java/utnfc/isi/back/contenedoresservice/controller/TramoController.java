package utnfc.isi.back.contenedoresservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.contenedoresservice.dto.TramoDTO;
import utnfc.isi.back.contenedoresservice.entity.Tramo;
import utnfc.isi.back.contenedoresservice.service.TramoService;

@RestController
@RequestMapping("/api/tramos")
@RequiredArgsConstructor
public class TramoController {

    private final TramoService tramoService;

    // TRANSPORTISTA: iniciar tramo
    @PreAuthorize("hasRole('transportista')")
    @PostMapping("/{idTramo}/inicio")
    public TramoDTO iniciarTramo(@PathVariable Long idTramo) {
        return tramoService.iniciarTramo(idTramo);
    }

    // TRANSPORTISTA: finalizar tramo
    @PreAuthorize("hasRole('transportista')")
    @PostMapping("/{idTramo}/fin")
    public TramoDTO finalizarTramo(@PathVariable Long idTramo) {
        return tramoService.finalizarTramo(idTramo);
    }

}
