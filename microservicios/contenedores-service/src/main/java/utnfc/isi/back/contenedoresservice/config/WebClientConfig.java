// Código en texto plano para WebClientConfig.java (Contenedores Service)

package utnfc.isi.back.contenedoresservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    /**
     * Define un bean de WebClient que será inyectado en CamionClient.
     * Es la forma estándar en WebFlux para comunicación HTTP.
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}