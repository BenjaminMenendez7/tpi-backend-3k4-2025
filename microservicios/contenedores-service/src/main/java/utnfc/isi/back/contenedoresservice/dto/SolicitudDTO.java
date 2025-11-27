package utnfc.isi.back.contenedoresservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@Getter @Setter
public class SolicitudDTO {
    @NotNull(message = "El id de la solicitud es obligatorio")
    private Long id;

    @NotNull(message = "El idCliente es obligatorio")
    private Long idCliente;

    private LocalDateTime fechaSolicitud;

    // FK al contenedor elegido
    @NotNull(message = "El idContenedor es obligatorio")
    private Long idContenedor;

    // FK al depósito (si aplica)
    private Long idDeposito;

    // FK a la ruta elegida (puede ser null al inicio)
    private Long idRuta;

    // Estado general de la solicitud (si lo manejás)
    private String estado;

    // Opcionales útiles para listar sin cargar detalle
    private BigDecimal costoTotal;
    private Long tiempoEstimadoMinutos;
}
