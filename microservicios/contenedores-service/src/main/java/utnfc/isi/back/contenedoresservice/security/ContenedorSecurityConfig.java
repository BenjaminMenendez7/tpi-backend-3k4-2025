package utnfc.isi.back.contenedoresservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import utnfc.isi.back.security.KeycloakJwtGrantedAuthoritiesConverter;

@Configuration
public class ContenedorSecurityConfig {

    // Convierte roles de Keycloak a GrantedAuthorities
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtGrantedAuthoritiesConverter());
        return converter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http,
            JwtAuthenticationConverter jwtAuthConverter) {

        http
                // Deshabilitar CSRF para APIs REST
                .csrf(csrf -> csrf.disable())

                // Configurar seguridad de rutas
                .authorizeExchange(auth -> auth
                        // Swagger / OpenAPI
                        // Se agrega la ruta /webjars/** para asegurar que los recursos estáticos de Swagger UI se carguen correctamente.
                        .pathMatchers("/v3/api-docs/**", "/swagger-ui/**", "/webjars/**").permitAll()

                        // ====== SOLICITUDES ======
                        .pathMatchers(HttpMethod.GET, "/solicitudes").hasRole("OPERADOR")
                        .pathMatchers(HttpMethod.POST, "/solicitudes").hasRole("CLIENTE")
                        .pathMatchers(HttpMethod.POST, "/solicitudes/{id}/asignar-camion/{idCamion}").hasRole("OPERADOR")
                        .pathMatchers(HttpMethod.POST, "/solicitudes/{id}/calcular").hasRole("OPERADOR")
                        .pathMatchers(HttpMethod.POST, "/solicitudes/{id}/estado/{nuevoEstadoId}").hasRole("OPERADOR")
                        .pathMatchers(HttpMethod.DELETE, "/solicitudes/{id}").hasRole("OPERADOR")
                        .pathMatchers(HttpMethod.GET, "/solicitudes/{id}/detalle").hasRole("CLIENTE")
                        .pathMatchers(HttpMethod.GET, "/solicitudes/{id}/seguimiento").hasRole("CLIENTE")
                        .pathMatchers(HttpMethod.GET, "/solicitudes/{id}/historial").hasRole("OPERADOR")

                        // ====== TRAMOS ======
                        .pathMatchers(HttpMethod.POST, "/tramos/{id}/inicio").hasRole("TRANSPORTISTA")
                        .pathMatchers(HttpMethod.POST, "/tramos/{id}/fin").hasRole("TRANSPORTISTA")

                        // Cualquier otra petición requiere autenticación
                        .anyExchange().authenticated()
                )

                // Configurar OAuth2 Resource Server con JWT y converter de Keycloak
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(
                                new ReactiveJwtAuthenticationConverterAdapter(jwtAuthConverter)
                        ))
                );

        return http.build();
    }
}