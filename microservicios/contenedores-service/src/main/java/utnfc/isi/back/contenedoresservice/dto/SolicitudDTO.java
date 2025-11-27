package utnfc.isi.back.contenedoresservice.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Getter @Setter
public class SolicitudDTO {

    private Long id;

    private LocalDateTime fechaSolicitud;

    // FK al contenedor elegido
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
