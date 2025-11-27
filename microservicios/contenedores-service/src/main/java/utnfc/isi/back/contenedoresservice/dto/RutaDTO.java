package utnfc.isi.back.contenedoresservice.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
public class RutaDTO {

    private Long id;

    // Distancia total en kilómetros
    private BigDecimal distanciaTotalKm;

    // Tiempo total estimado en minutos
    private Long tiempoTotalMinutos;

    // Costo total de la ruta
    private BigDecimal costoTotal;

    // True si esta ruta fue la seleccionada
    private boolean seleccionada;

    // ID de la solicitud a la que pertenece
    private Long idSolicitud;

    private String origenNombre;
    private String destinoNombre;

}
