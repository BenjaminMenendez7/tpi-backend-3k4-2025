package utnfc.isi.back.contenedoresservice.service.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum EstadoSolicitudEnum {
    borrador(1L), programada(2L), en_transito(3L), entregada(4L);

    private final Long id;

    EstadoSolicitudEnum(Long id) { this.id = id; }

    public static EstadoSolicitudEnum fromId(Long id) {
        return Arrays.stream(values())
                .filter(e -> e.id.equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("ID de estado inválido: " + id));
    }
}
