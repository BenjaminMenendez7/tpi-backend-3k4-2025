package utnfc.isi.back.clientesservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class GeocodingService {

    private final WebClient webClient;

    public GeocodingService() {
        this.webClient = WebClient.create("https://nominatim.openstreetmap.org");
    }

    public Mono<Coordenada> geocode(String direccion) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("format", "json")
                        .queryParam("limit", "1") // solo la primera coincidencia
                        .queryParam("q", direccion)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> {
                    if (json.isArray() && json.size() > 0) {
                        JsonNode nodo = json.get(0);
                        double lat = nodo.get("lat").asDouble();
                        double lon = nodo.get("lon").asDouble();
                        return new Coordenada(lat, lon);
                    } else {
                        throw new RuntimeException("No se encontró coordenada para: " + direccion);
                    }
                });
    }

    public static class Coordenada {
        private final double lat;
        private final double lon;

        public Coordenada(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public double getLat() { return lat; }
        public double getLon() { return lon; }
    }
}
