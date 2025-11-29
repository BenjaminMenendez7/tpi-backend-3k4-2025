package utnfc.isi.back.gatewayservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class SwaggerAggregatorController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/v3/api-docs/combined")
    public String getCombinedSwagger() {

        String clientes = restTemplate.getForObject(
                "http://clientes-service:8081/v3/api-docs", String.class);

        String camiones = restTemplate.getForObject(
                "http://camiones-service:8082/v3/api-docs", String.class);

        String contenedores = restTemplate.getForObject(
                "http://contenedores-service:8083/v3/api-docs", String.class);

        // Concatenamos por simplicidad
        return "{ \"clientes\": " + clientes +
                ", \"camiones\": " + camiones +
                ", \"contenedores\": " + contenedores + " }";
    }
}
