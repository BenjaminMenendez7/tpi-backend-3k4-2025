package utnfc.isi.back.camionesservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para recibir datos de creación/actualización de tarifas desde el cliente.
 * Incluye costos y rangos, pero no el campo calculado "total".
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearTarifaRequestDTO {

    @NotNull(message = "El costo por km no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = true, message = "El costo por km debe ser mayor o igual a 0")
    private BigDecimal costoKm;

    @NotNull(message = "El costo de combustible no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = true, message = "El costo de combustible debe ser mayor o igual a 0")
    private BigDecimal costoCombustible;

    @NotNull(message = "El costo de estadía en depósito no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = true, message = "El costo de estadía debe ser mayor o igual a 0")
    private BigDecimal costoEstadiaDeposito;

    @NotNull(message = "El idTipoCamion es obligatorio")
    private Long idTipoCamion;

    @NotNull(message = "El rango de peso es obligatorio")
    private String rangoPeso;

    @NotNull(message = "El rango de volumen es obligatorio")
    private String rangoVolumen;
}
