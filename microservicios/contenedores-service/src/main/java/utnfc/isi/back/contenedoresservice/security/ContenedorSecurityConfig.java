// Código en texto plano para ContenedorSecurityConfig.java (Contenedores Service - Versión Final)

package utnfc.isi.back.contenedoresservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity; // ⬅️ NUEVO: Habilita @PreAuthorize en WebFlux
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.AuthorizeExchangeSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.OAuth2ResourceServerSpec;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity; // ⬅️ NUEVO: Inicializa beans reactivos

@Configuration
@EnableWebFluxSecurity // 🛑 CRÍTICO: Activa la configuración de seguridad Reactiva (crea ServerHttpSecurity)
@EnableReactiveMethodSecurity // Habilita @PreAuthorize en WebFlux
public class ContenedorSecurityConfig {

    private final JwtAuthenticationConverter jwtAuthConverter;

    public ContenedorSecurityConfig(JwtAuthenticationConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http) {

        http
                // Deshabilitar CSRF para APIs REST
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // Configurar seguridad de rutas
                .authorizeExchange(auth -> {
                    AuthorizeExchangeSpec a = (AuthorizeExchangeSpec) auth;

                    // Swagger / OpenAPI
                    a.pathMatchers(
                            "/v3/api-docs/**",
                            "/swagger-ui.html",
                            "/swagger-ui/**",
                            "/webjars/**",
                            "/docs/**",          // si pusiste tus propios assets en /docs/
                            "/favicon.ico").permitAll();


                    // ====== SOLICITUDES ======
                    // Nota: Se usa /api/solicitudes para que coincida con el RequestMapping del Controller.
                    a.pathMatchers(HttpMethod.GET, "/api/solicitudes").hasRole("OPERADOR");
                    a.pathMatchers(HttpMethod.POST, "/api/solicitudes").hasRole("CLIENTE");

                    // Asignación, Cálculo, Cambio de Estado
                    a.pathMatchers(HttpMethod.POST, "/api/solicitudes/{id}/asignar-camion/{idCamion}").hasRole("OPERADOR");
                    a.pathMatchers(HttpMethod.POST, "/api/solicitudes/{id}/calcular").hasRole("OPERADOR");
                    a.pathMatchers(HttpMethod.PUT, "/api/solicitudes/{id}/estado/{nuevoEstadoId}").hasRole("OPERADOR");

                    // Eliminación
                    a.pathMatchers(HttpMethod.DELETE, "/api/solicitudes/{id}").hasRole("OPERADOR");

                    // Lectura (Detalle, Seguimiento, Historial)
                    a.pathMatchers(HttpMethod.GET, "/api/solicitudes/{id}/detalle").hasRole("CLIENTE");
                    a.pathMatchers(HttpMethod.GET, "/api/solicitudes/{id}/seguimiento").hasRole("CLIENTE");
                    a.pathMatchers(HttpMethod.GET, "/api/solicitudes/{id}/historial").hasRole("OPERADOR");

                    // ====== TRAMOS ======
                    a.pathMatchers(HttpMethod.POST, "/api/tramos/{id}/inicio").hasRole("TRANSPORTISTA");
                    a.pathMatchers(HttpMethod.POST, "/api/tramos/{id}/fin").hasRole("TRANSPORTISTA");

                    // Cualquier otra petición requiere autenticación
                    a.anyExchange().authenticated();
                })

                // Configurar OAuth2 Resource Server para WebFlux
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(
                                new ReactiveJwtAuthenticationConverterAdapter(this.jwtAuthConverter)
                        ))
                );

        return http.build();
    }
}