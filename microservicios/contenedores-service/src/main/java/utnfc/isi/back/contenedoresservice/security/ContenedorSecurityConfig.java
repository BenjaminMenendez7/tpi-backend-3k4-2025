package utnfc.isi.back.contenedoresservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import reactor.core.publisher.Mono;
import utnfc.isi.back.security.KeycloakJwtGrantedAuthoritiesConverter;

import java.time.Instant;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class ContenedorSecurityConfig {

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtGrantedAuthoritiesConverter());
        return converter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        // Handler personalizado para 403 Forbidden
        ServerAccessDeniedHandler accessDeniedHandler = (exchange, denied) -> {
            var response = exchange.getResponse();
            response.setStatusCode(HttpStatus.FORBIDDEN);
            response.getHeaders().add("Content-Type", "application/json");

            String body = """
                {
                  "timestamp": "%s",
                  "status": 403,
                  "error": "Forbidden",
                  "message": "No tiene permisos para realizar esta acción",
                  "path": "%s"
                }
                """.formatted(Instant.now(), exchange.getRequest().getPath());

            var buffer = response.bufferFactory().wrap(body.getBytes());
            return response.writeWith(Mono.just(buffer));
        };

        ServerAuthenticationEntryPoint authenticationEntryPoint = (exchange, ex) -> {
            var response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("Content-Type", "application/json");

            String body = """
                {
                  "timestamp": "%s",
                  "status": 401,
                  "error": "Unauthorized",
                  "message": "Token inválido o ausente",
                  "path": "%s"
                }
                """.formatted(Instant.now(), exchange.getRequest().getPath());

            var buffer = response.bufferFactory().wrap(body.getBytes());
            return response.writeWith(Mono.just(buffer));
        };

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
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

                        .pathMatchers(HttpMethod.GET, "/solicitudes").hasRole("OPERADOR")
                        .pathMatchers(HttpMethod.POST, "/solicitudes").hasRole("CLIENTE")
                        .pathMatchers(HttpMethod.POST, "/solicitudes/{id}/asignar-camion/{idCamion}").hasRole("OPERADOR")
                        .pathMatchers(HttpMethod.POST, "/solicitudes/{id}/calcular").hasRole("OPERADOR")
                        .pathMatchers(HttpMethod.PUT, "/solicitudes/{id}/estado/{nuevoEstadoId}").hasRole("OPERADOR")
                        .pathMatchers(HttpMethod.DELETE, "/solicitudes/{id}").hasRole("OPERADOR")
                        .pathMatchers(HttpMethod.GET, "/solicitudes/{id}/detalle").hasRole("CLIENTE")
                        .pathMatchers(HttpMethod.GET, "/solicitudes/{id}/seguimiento").hasRole("CLIENTE")
                        .pathMatchers(HttpMethod.GET, "/solicitudes/{id}/historial").hasRole("OPERADOR")

                        .pathMatchers(HttpMethod.POST, "/tramos/{id}/inicio").hasRole("TRANSPORTISTA")
                        .pathMatchers(HttpMethod.POST, "/tramos/{id}/fin").hasRole("TRANSPORTISTA")

                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(
                                new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter())
                        ))
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler))
                .build();
    }
}
