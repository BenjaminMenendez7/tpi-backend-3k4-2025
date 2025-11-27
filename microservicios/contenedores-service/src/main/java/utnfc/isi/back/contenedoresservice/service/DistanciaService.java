package utnfc.isi.back.contenedoresservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import utnfc.isi.back.contenedoresservice.dto.GeolocalizacionDTO;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DistanciaService {

    private final OsrmService osrmService;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String URL_MS_CLIENTES = "http://localhost:8081/api/geolocalizacion/";

    public BigDecimal obtenerDistanciaDesdeIds(Long idDirOrigen, Long idDirDestino) {

        GeolocalizacionDTO origen = restTemplate.getForObject(
                URL_MS_CLIENTES + idDirOrigen, GeolocalizacionDTO.class);

        GeolocalizacionDTO destino = restTemplate.getForObject(
                URL_MS_CLIENTES + idDirDestino, GeolocalizacionDTO.class);

        return osrmService.obtenerDistanciaKm(
                BigDecimal.valueOf(origen.getLatitud()),
                BigDecimal.valueOf(origen.getLongitud()),
                BigDecimal.valueOf(destino.getLatitud()),
                BigDecimal.valueOf(destino.getLongitud())
        );
    }

}

