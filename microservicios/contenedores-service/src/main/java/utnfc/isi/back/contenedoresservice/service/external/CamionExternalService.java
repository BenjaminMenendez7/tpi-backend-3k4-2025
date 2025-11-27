package utnfc.isi.back.contenedoresservice.service.external;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import utnfc.isi.back.contenedoresservice.dto.CamionDTO;

import java.util.Arrays;
import java.util.List;

@Service
public class CamionExternalService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String BASE_URL = "http://camiones-service:8082/api/camiones";

    public List<CamionDTO> obtenerDisponibles() {
        CamionDTO[] respuesta = restTemplate.getForObject(
                BASE_URL + "/disponibles",
                CamionDTO[].class
        );

        if (respuesta == null) {
            throw new RuntimeException("Error obteniendo camiones disponibles desde camiones-service");
        }

        return Arrays.asList(respuesta);
    }
}
