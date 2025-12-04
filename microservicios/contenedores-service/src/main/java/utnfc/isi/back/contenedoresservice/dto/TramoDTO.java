package utnfc.isi.back.contenedoresservice.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TramoDTO {
    private Long id;
    private CamionDTO camion;
    private Long origen;
    private Long destino;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private BigDecimal costoAproximado;
    private BigDecimal costoReal;
    private BigDecimal distanciaKm;
    private Long tiempoEstimadoMinutos;
    private RutaDTO ruta;
    private TipoTramoDTO tipoTramo;
    private String estadoTramo;
}

