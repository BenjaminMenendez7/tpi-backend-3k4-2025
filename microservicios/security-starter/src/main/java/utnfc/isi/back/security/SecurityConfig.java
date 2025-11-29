package utnfc.isi.back.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

// 🛑 IMPORTANTE: Asegúrate de que KeycloakJwtGrantedAuthoritiesConverter existe y no está duplicada.

@Configuration
public class SecurityConfig {

    /**
     * Provee el bean que define cómo Spring Security debe extraer los roles (GrantedAuthorities)
     * del JWT usando la lógica de Keycloak.
     * @return JwtAuthenticationConverter configurado.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthConverter() {
        // 1. Crear el conversor principal de autenticación
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        // 2. Asignar el conversor que extrae los GrantedAuthorities del JWT (roles).
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtGrantedAuthoritiesConverter());

        return converter;
    }
}