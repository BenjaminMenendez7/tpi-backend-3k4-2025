package utnfc.isi.back.contenedoresservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration; // ⬅️ excluye seguridad servlet
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;   // ⬅️ excluye MVC
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@ComponentScan({
        "utnfc.isi.back.security",              // módulo compartido
        "utnfc.isi.back.contenedoresservice"    // código propio del microservicio
})
@SpringBootApplication(
        exclude = {
                SecurityAutoConfiguration.class, // evita configuración de seguridad servlet
                WebMvcAutoConfiguration.class    // evita configuración de MVC
        }
)
@EnableWebFluxSecurity              // activa seguridad reactiva
@EnableReactiveMethodSecurity        // habilita @PreAuthorize en WebFlux
public class ContenedoresServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContenedoresServiceApplication.class, args);
    }

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder(
            @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwkSetUri) {
        // Usa NimbusReactiveJwtDecoder para validar JWT en WebFlux
        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}
