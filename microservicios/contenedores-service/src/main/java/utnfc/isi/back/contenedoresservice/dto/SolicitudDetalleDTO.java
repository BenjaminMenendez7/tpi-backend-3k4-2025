package utnfc.isi.back.contenedoresservice.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SolicitudDetalleDTO {

    private Long id;
    private LocalDateTime fechaSolicitud;

    private ContenedorDTO contenedor;
    private DepositoDTO deposito;
    private RutaDTO ruta;
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
