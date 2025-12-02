package utnfc.isi.back.camionesservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarifaRequestDTO {

    // --- Campos para CREAR/ACTUALIZAR tarifas ---
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

    // --- Campos para BUSCAR tarifas ---
    @DecimalMin(value = "0.0", inclusive = true, message = "El peso del contenedor debe ser mayor o igual a 0")
    private BigDecimal pesoContenedor;

    @DecimalMin(value = "0.0", inclusive = true, message = "El volumen del contenedor debe ser mayor o igual a 0")
    private BigDecimal volumenContenedor;
}
