package utnfc.isi.back.contenedoresservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import utnfc.isi.back.security.KeycloakJwtGrantedAuthoritiesConverter;

@Configuration
@EnableWebFluxSecurity              // Activa seguridad reactiva
@EnableReactiveMethodSecurity       // Habilita @PreAuthorize en WebFlux
public class ContenedorSecurityConfig {

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtGrantedAuthoritiesConverter());
        return converter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                // Deshabilitar CSRF para APIs REST
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // Configuración de rutas
                .authorizeExchange(auth -> auth
                        // Swagger / OpenAPI
                        .pathMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/docs/**",
                                "/favicon.ico"
                        ).permitAll()

                        // ====== SOLICITUDES ======
                        .pathMatchers(HttpMethod.GET, "/solicitudes").hasRole("OPERADOR")
                        .pathMatchers(HttpMethod.POST, "/solicitudes").hasRole("CLIENTE")

                        .pathMatchers(HttpMethod.POST, "/solicitudes/{id}/asignar-camion/{idCamion}").hasRole("OPERADOR")
                        .pathMatchers(HttpMethod.POST, "/solicitudes/{id}/calcular").hasRole("OPERADOR")
                        .pathMatchers(HttpMethod.PUT, "/solicitudes/{id}/estado/{nuevoEstadoId}").hasRole("OPERADOR")

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

                // Configurar OAuth2 Resource Server con JWT
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(
                                new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter())
                        ))
                )

                .build();
    }
}
