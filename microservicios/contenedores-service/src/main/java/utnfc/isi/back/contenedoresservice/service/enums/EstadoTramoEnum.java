package utnfc.isi.back.contenedoresservice.service.enums;

public enum EstadoTramoEnum {
    estimado(1L), asignado(2L), iniciado(3L), finalizado(4L);

    private Long id;

    EstadoTramoEnum(Long id) {
        this.id = id;
    }
}
