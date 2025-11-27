package utnfc.isi.back.contenedoresservice.client;

import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import utnfc.isi.back.contenedoresservice.dto.CamionDTO;

@Service
@RequiredArgsConstructor
public class CamionClient {

    private final RestTemplate restTemplate;

    private final String BASE_URL = "http://camiones-service:8082/api/camiones";

    public CamionDTO obtenerCamion(Long idCamion) {
        return restTemplate.getForObject(
                BASE_URL + "/" + idCamion,
                CamionDTO.class
        );
    }

    public void marcarNoDisponible(Long idCamion) {
        restTemplate.put(BASE_URL + "/" + idCamion + "/ocupar", null);
    }

    public void marcarDisponible(Long idCamion) {
        restTemplate.put(BASE_URL + "/" + idCamion + "/liberar", null);
    }

}

