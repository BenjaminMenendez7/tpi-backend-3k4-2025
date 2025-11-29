package utnfc.isi.back.contenedoresservice.service.enums;

public enum EstadoTramoEnum {
    BORRADOR(1L), INICIADO(2L), FINALIZADO(3L);

    private Long id;

    EstadoTramoEnum(Long id) {
        this.id = id;
    }
}
