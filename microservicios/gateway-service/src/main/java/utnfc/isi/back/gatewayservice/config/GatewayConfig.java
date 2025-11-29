package utnfc.isi.back.gatewayservice.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class GatewayConfig {

    private GatewayFilter authHeaderForwardFilter() {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader != null) {
                return chain.filter(exchange.mutate()
                        .request(r -> r.header(HttpHeaders.AUTHORIZATION, authHeader))
                        .build());
            }
            return chain.filter(exchange);
        };
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        GatewayFilter authForwardFilter = authHeaderForwardFilter();

        return builder.routes()

                // CLIENTES SERVICE
                .route("clientes-service", r -> r.path("/api/clientes/**", "/api/direcciones/**", "/api/geolocalizacion/**")
                        .filters(f -> f
                                .filter(authForwardFilter)
                                .stripPrefix(1)
                                .preserveHostHeader()
                        )
                        .uri("http://clientes-service:8081"))

                // CAMIONES SERVICE
                .route("camiones-service", r -> r.path("/api/camiones/**", "/api/tarifas/**")
                        .filters(f -> f
                                .filter(authForwardFilter)
                                .stripPrefix(1)
                                .preserveHostHeader()
                        )
                        .uri("http://camiones-service:8082"))

                // CONTENEDORES SERVICE
                .route("contenedores-service", r -> r.path("/api/solicitudes/**", "/api/rutas/**", "/api/tramos/**")
                        .filters(f -> f
                                .filter(authForwardFilter)
                                .stripPrefix(1)
                                .preserveHostHeader()
                        )
                        .uri("http://contenedores-service:8083"))

                // OSRM SERVICE
                .route("osrm-service", r -> r.path("/api/osrm/**")
                        .filters(f -> f
                                .filter(authForwardFilter)
                                .stripPrefix(1)
                                .preserveHostHeader()
                        )
                        .uri("http://osrm:5000"))

                .build();
    }
}
