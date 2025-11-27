package utnfc.isi.back.contenedoresservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class DireccionClient {

    private static final String BASE_URL = "http://clientes-service:8081/api/direcciones/";

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> obtenerDireccion(Long idDireccion) {
        return restTemplate.getForObject(BASE_URL + idDireccion, Map.class);
    }
}

