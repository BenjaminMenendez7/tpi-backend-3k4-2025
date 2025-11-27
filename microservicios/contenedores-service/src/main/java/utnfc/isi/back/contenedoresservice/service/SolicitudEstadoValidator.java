package utnfc.isi.back.contenedoresservice.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class SolicitudEstadoValidator {

    private static final Map<Long, Set<Long>> TRANSICIONES_VALIDAS = Map.of(
            1L, Set.of(2L), // borrador -> programada
            2L, Set.of(3L), // programada -> en transito
            3L, Set.of(4L)  // en transito -> entregada
    );

    public boolean puedeCambiar(Long estadoActual, Long nuevoEstado) {
        return TRANSICIONES_VALIDAS.getOrDefault(estadoActual, Set.of())
                .contains(nuevoEstado);
    }

    public Set<Long> getPermitidos(Long estadoActual) {
        return TRANSICIONES_VALIDAS.getOrDefault(estadoActual, Set.of());
    }

    public Set<Long> getPermitidosIds(Long estadoActual) {
        return TRANSICIONES_VALIDAS.getOrDefault(estadoActual, Set.of());
    }

}
