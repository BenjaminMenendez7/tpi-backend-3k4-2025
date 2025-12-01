package utnfc.isi.back.camionesservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import utnfc.isi.back.security.KeycloakJwtGrantedAuthoritiesConverter;

import java.time.Instant;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class CamionesSecurityConfig {

    private final JwtAuthenticationConverter jwtAuthConverter;

    public CamionesSecurityConfig(JwtAuthenticationConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtGrantedAuthoritiesConverter());
        return converter;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.POST, "/camiones/**", "/tarifas/**")
                        .hasRole("OPERADOR")
                        .requestMatchers(HttpMethod.PUT, "/camiones/**", "/tarifas/**")
                        .hasRole("OPERADOR")
                        .requestMatchers(HttpMethod.DELETE, "/camiones/**", "/tarifas/**")
                        .hasRole("OPERADOR")

                        .requestMatchers(HttpMethod.GET, "/tarifas/**")
                        .hasRole("OPERADOR")

                        .requestMatchers(HttpMethod.GET, "/camiones/**")
                        .hasRole("OPERADOR")
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        .anyRequest().authenticated()
                )

                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(this.jwtAuthConverter))
                ).exceptionHandling(ex -> ex
                        .accessDeniedHandler((req, res, excep) -> {
                            res.setStatus(HttpStatus.FORBIDDEN.value());
                            res.setContentType("application/json");
                            res.getWriter().write("""
                        {
                          "timestamp": "%s",
                          "status": 403,
                          "error": "Forbidden",
                          "message": "No tiene permisos para realizar esta acción"
                        }
                        """.formatted(Instant.now(), req.getRequestURI()));
                        })
                );

        return http.build();
    }
}
