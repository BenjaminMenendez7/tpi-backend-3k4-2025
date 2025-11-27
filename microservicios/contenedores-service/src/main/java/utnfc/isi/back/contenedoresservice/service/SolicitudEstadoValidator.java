package utnfc.isi.back.contenedoresservice.service;

import org.springframework.stereotype.Component;
import utnfc.isi.back.contenedoresservice.service.enums.EstadoSolicitudEnum;

import java.util.Map;
import java.util.Set;

@Component
public class SolicitudEstadoValidator {

    private static final Map<EstadoSolicitudEnum, Set<EstadoSolicitudEnum>>
            TRANSICIONES_VALIDAS = Map.of(
                    EstadoSolicitudEnum.BORRADOR, Set.of(EstadoSolicitudEnum.PROGRAMADA),
                    EstadoSolicitudEnum.PROGRAMADA, Set.of(EstadoSolicitudEnum.EN_TRANSITO),
                    EstadoSolicitudEnum.EN_TRANSITO, Set.of(EstadoSolicitudEnum.ENTREGADA)
    );

    public boolean puedeCambiar(EstadoSolicitudEnum actual,  EstadoSolicitudEnum nuevo) {
        return TRANSICIONES_VALIDAS.getOrDefault(actual, Set.of()).contains(nuevo);
    }

    public Set<EstadoSolicitudEnum> getPermitidos(EstadoSolicitudEnum actual) {
        return TRANSICIONES_VALIDAS.getOrDefault(actual, Set.of());
    }
}
