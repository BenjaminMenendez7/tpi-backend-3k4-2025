package utnfc.isi.back.clientesservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ClienteSecurityConfig {

    private final JwtAuthenticationConverter jwtAuthConverter;

    public ClienteSecurityConfig(JwtAuthenticationConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints internos consumidos por microservicios (requieren JWT válido)
                        .requestMatchers("/direcciones/**").authenticated()
                        .requestMatchers("/geolocalizacion/**").authenticated()

                        // Endpoints para usuarios humanos, requieren rol OPERADOR
                        .requestMatchers("/clientes/**").hasRole("OPERADOR")
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Cualquier otro request autenticado
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(this.jwtAuthConverter))
                );

        return http.build();
    }
}
