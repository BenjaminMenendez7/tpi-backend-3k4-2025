package utnfc.isi.back.contenedoresservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDTO {
    @NotNull(message = "El id de la solicitud es obligatorio")
    private Long id;

    @NotNull(message = "El idCliente es obligatorio")
    private Long idCliente;

    // FK al contenedor elegido
    @NotNull(message = "El idContenedor es obligatorio")
    private Long idContenedor;
}
