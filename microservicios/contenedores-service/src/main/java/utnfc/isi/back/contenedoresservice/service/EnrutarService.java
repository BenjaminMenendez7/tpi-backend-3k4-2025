package utnfc.isi.back.contenedoresservice.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class EnrutarService {

    private final WebClient.Builder webClientBuilder;

    public EnrutarService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    /**
     * Método genérico para obtener datos de cualquier endpoint de clientes-service.
     * @param path Endpoint completo después de /api/, ej: "direcciones/1" o "geolocalizacion/2"
     * @param jwt JWT del usuario actual
     * @return Object con la respuesta del microservicio
     */
    public Object obtenerDesdeClientes(String path, Jwt jwt) {
        String token = jwt.getTokenValue();

        return webClientBuilder.build()
                .get()
                .uri("http://clientes-service:8081/api/{path}", path)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        resp -> Mono.error(new RuntimeException("Error cliente-service: " + resp.statusCode())))
                .onStatus(status -> status.is5xxServerError(),
                        resp -> Mono.error(new RuntimeException("Error interno cliente-service: " + resp.statusCode())))
                .bodyToMono(Object.class)
                .block();
    }
}
