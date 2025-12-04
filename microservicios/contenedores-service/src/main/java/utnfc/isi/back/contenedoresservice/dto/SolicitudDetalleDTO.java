package utnfc.isi.back.contenedoresservice.dto;

import lombok.*;
import utnfc.isi.back.contenedoresservice.entity.SolicitudEstadoHistorial;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDetalleDTO {
    private Long id;
    private Long idCliente;
    private LocalDateTime fechaSolicitud;
    private String estadoSolicitud;
    private ContenedorDTO contenedor;
    private RutaDTO ruta;
    private List<TramoDTO> tramos;
    private CamionDTO camion;
    private BigDecimal costoEstimado;
    private BigDecimal costoFinal;
    private BigDecimal tiempoReal;
    private HistorialEstadoDTO historialEstado;
    private DepositoDTO deposito;
    private BigDecimal costoTotal;
    private Long tiempoEstimadoMinutos;
    private BigDecimal costoEstadia;
    private BigDecimal costoCombustible;
    private BigDecimal costoKilometros;
    private Long estadiaRealMinutos;
}
