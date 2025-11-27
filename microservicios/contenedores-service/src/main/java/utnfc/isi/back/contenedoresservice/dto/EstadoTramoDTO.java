package utnfc.isi.back.contenedoresservice.dto;

import lombok.*;

@Getter @Setter
public class EstadoTramoDTO {

    private Long id;

    private String nombre;
    // Ej: "PENDIENTE", "EN_PROCESO", "COMPLETADO"
}
