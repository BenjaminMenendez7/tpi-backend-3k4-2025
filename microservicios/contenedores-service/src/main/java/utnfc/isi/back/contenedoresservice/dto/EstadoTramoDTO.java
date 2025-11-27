package utnfc.isi.back.contenedoresservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
public class EstadoTramoDTO {
    @NotNull
    private Long id;

    private String nombre;
    // Ej: "PENDIENTE", "EN_PROCESO", "COMPLETADO"
}
