package utnfc.isi.back.camionesservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class CamionesSecurityConfig {

    private final JwtAuthenticationConverter jwtAuthConverter;

    public CamionesSecurityConfig(JwtAuthenticationConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // =======================
                        // OPERADOR (CRUD COMPLETO)
                        // =======================
                        .requestMatchers(HttpMethod.POST, "/camiones/**", "/tarifas/**")
                        .hasRole("OPERADOR")
                        .requestMatchers(HttpMethod.PUT, "/camiones/**", "/tarifas/**")
                        .hasRole("OPERADOR")
                        .requestMatchers(HttpMethod.DELETE, "/camiones/**", "/tarifas/**")
                        .hasRole("OPERADOR")

                        // =======================
                        // GET /tarifas  (OPERADOR)
                        // =======================
                        // Cambiado: evitamos CLIENTE ya que NO existe en tu JWT
                        .requestMatchers(HttpMethod.GET, "/tarifas/**")
                        .hasRole("OPERADOR")

                        // =======================
                        // GET /camiones (OPERADOR)
                        // =======================
                        // TRANSPORTISTA no existe en tu JWT → se remueve temporalmente
                        .requestMatchers(HttpMethod.GET, "/camiones/**")
                        .hasRole("OPERADOR")
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // Cualquier otra petición autenticada
                        .anyRequest().authenticated()
                )

                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(this.jwtAuthConverter))
                );

        return http.build();
    }
}
