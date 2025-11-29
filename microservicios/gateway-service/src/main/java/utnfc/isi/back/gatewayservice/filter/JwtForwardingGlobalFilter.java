package utnfc.isi.back.gatewayservice.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Filtro Global para reenviar el Header Authorization de forma segura.
 * Esto evita el error 500 (IllegalArgumentException) que ocurría al intentar
 * copiar el header nulo usando la sintaxis SpEL en el YAML/Java.
 */
@Component
public class JwtForwardingGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        List<String> authHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

        // 1. 🟢 CASO SEGURO: Si el header es nulo o vacío (el error 500)
        // 🛑 En lugar de fallar, simplemente dejamos el header como está y pasamos la cadena.
        if (authHeaders == null || authHeaders.isEmpty()) {
            // Pasamos la cadena sin modificar el header. El filtro 'addRequestHeader'
            // del GatewayConfig fallará si se intenta acceder a un valor nulo,
            // pero si no se intenta acceder (lo cual es complejo), podría funcionar.
            // La solución más limpia es simplemente pasar la solicitud sin el header.
            return chain.filter(exchange);
        }

        // 2. CASO VÁLIDO: El token existe.
        String token = authHeaders.get(0);

        // Creamos una nueva solicitud con el token copiado
        ServerHttpRequest request = exchange.getRequest().mutate()
                .header(HttpHeaders.AUTHORIZATION, token)
                .build();

        // Pasamos el exchange con la solicitud modificada
        return chain.filter(exchange.mutate().request(request).build());
    }

    // Usamos un orden muy bajo para asegurar que este filtro se ejecute primero,
    // antes de que cualquier otro filtro falle por falta del header.
    @Override
    public int getOrder() {
        return -100;
    }
}