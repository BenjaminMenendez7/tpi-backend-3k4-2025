// Código en texto plano para ContenedoresServiceApplication.java (Versión Final)

package utnfc.isi.back.contenedoresservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration; // ⬅️ CRÍTICO
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

@ComponentScan({
        "utnfc.isi.back.security",
        "utnfc.isi.back.contenedoresservice"
})
@SpringBootApplication(
        // 🛑 SOLUCIÓN FINAL: Excluir la seguridad de MVC y la configuración de Web MVC
        exclude = {
                SecurityAutoConfiguration.class, // ⬅️ Excluye la auto-configuración de la seguridad de la pila Servlet
                WebMvcAutoConfiguration.class    // ⬅️ Excluye la configuración de la pila Web MVC
        }
)
@EnableMethodSecurity(prePostEnabled = true)
public class ContenedoresServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContenedoresServiceApplication.class, args);
    }
    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwkSetUri) {
        // Usa Nimbus ReactiveJwtDecoder para la seguridad WebFlux
        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}