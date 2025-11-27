package utnfc.isi.back.contenedoresservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import utnfc.isi.back.contenedoresservice.dto.CamionDTO;

@Service
@RequiredArgsConstructor
public class CamionClient {

    private final WebClient webClient;

    @Value("${camiones.service.url}")
    private String baseUrl;

    public Mono<CamionDTO> obtenerCamion(Long idCamion) {
        return webClient.get()
                .uri(baseUrl + "/" + idCamion)
                .retrieve()
                .bodyToMono(CamionDTO.class);
    }

    public Mono<Void> marcarNoDisponible(Long idCamion) {
        return webClient.put()
                .uri(baseUrl + "/" + idCamion + "/ocupar")
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> marcarDisponible(Long idCamion) {
        return webClient.put()
                .uri(baseUrl + "/" + idCamion + "/liberar")
                .retrieve()
                .bodyToMono(Void.class);
    }
}
