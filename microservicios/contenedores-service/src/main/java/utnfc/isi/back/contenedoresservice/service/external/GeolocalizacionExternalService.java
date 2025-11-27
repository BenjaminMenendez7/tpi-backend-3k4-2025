package utnfc.isi.back.contenedoresservice.service.external;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import utnfc.isi.back.contenedoresservice.dto.external.GeolocalizacionDTO;

@Service
@RequiredArgsConstructor
public class GeolocalizacionExternalService {

    private final RestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:8081/api/geolocalizacion/";

    public GeolocalizacionDTO obtenerCoordenadas(Long idDireccion) {
        String url = BASE_URL + idDireccion;

        try {
            return restTemplate.getForObject(url, GeolocalizacionDTO.class);
        } catch (Exception e) {
            System.out.println("[GeolocalizacionExternalService] ERROR llamando a: " + url);
            return null;
        }
    }
}
