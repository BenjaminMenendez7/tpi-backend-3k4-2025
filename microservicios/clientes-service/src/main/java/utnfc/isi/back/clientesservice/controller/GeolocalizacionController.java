package utnfc.isi.back.clientesservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.clientesservice.dto.GeolocalizacionDTO;
import utnfc.isi.back.clientesservice.service.GeolocalizacionService;

@RestController
@RequestMapping("/geolocalizacion")
@RequiredArgsConstructor
public class GeolocalizacionController {

    private final GeolocalizacionService geoService;

    @GetMapping("/{idDireccion}")
    public GeolocalizacionDTO obtener(@PathVariable Long idDireccion) {
        var geo = geoService.obtenerPorIdDireccion(idDireccion);
        if (geo == null) return null;

        GeolocalizacionDTO dto = new GeolocalizacionDTO();
        dto.setLatitud(geo.getLatitud().doubleValue());
        dto.setLongitud(geo.getLongitud().doubleValue());
        return dto;
    }

}
