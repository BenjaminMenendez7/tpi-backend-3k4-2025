package utnfc.isi.back.contenedoresservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
public class SolicitudDetalleDTO {
    @NotNull(message = "El idDetalle es obligatorio")
    private Long id;
    private LocalDateTime fechaSolicitud;

    @NotNull
    private ContenedorDTO contenedor;

    @NotNull
    private DepositoDTO deposito;

    @NotNull
    private RutaDTO ruta;

    @NotNull
    private List<TramoDTO> tramos;

    private String estado;

    private BigDecimal costoTotal;
    private Long tiempoEstimadoMinutos;

    private BigDecimal costoEstadia;
    private BigDecimal costoCombustible;
    private BigDecimal costoKilometros;

    private CamionDTO camion;
    private List<HistorialEstadoDTO> historialEstados;

    private BigDecimal costoFinal;
    private BigDecimal tiempoReal;

    private Long estadiaRealMinutos;



}
