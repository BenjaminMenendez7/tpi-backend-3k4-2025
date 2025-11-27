package utnfc.isi.back.contenedoresservice.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
public class TramoDTO {

    private Long id;

    private Long idRuta;
    private Long idCamion;
    private Long idTipoTramo;
    private Long idEstadoTramo;
    private Long origen;
    private Long destino;

    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;

    private BigDecimal costoAproximado;
    private BigDecimal costoReal;

    private BigDecimal distanciaKm;
    private Long tiempoEstimadoMinutos;

    private String origenNombre;
    private String destinoNombre;

    private String estadoNombre;

}

