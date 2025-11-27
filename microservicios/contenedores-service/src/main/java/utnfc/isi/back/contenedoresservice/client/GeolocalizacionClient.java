package utnfc.isi.back.contenedoresservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GeolocalizacionClient {

    private static final String BASE_URL = "http://localhost:8081/api/geolocalizacion/";

    private final RestTemplate restTemplate = new RestTemplate();

    public CoordenadasDTO obtenerCoordenadas(Long idDireccion) {
        return restTemplate.getForObject(BASE_URL + idDireccion, CoordenadasDTO.class);
    }
}

