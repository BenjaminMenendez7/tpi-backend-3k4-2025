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
public class TarifaDTO {

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal costoKm;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal costoCombustible;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal costoEstadiaDeposito;

    /**
     * Helper para calcular el total de la tarifa sumando
     * costo por km + combustible + estadía.
     */
    public BigDecimal calcularTotal() {
        BigDecimal total = BigDecimal.ZERO;
        if (costoKm != null) total = total.add(costoKm);
        if (costoCombustible != null) total = total.add(costoCombustible);
        if (costoEstadiaDeposito != null) total = total.add(costoEstadiaDeposito);
        return total;
    }

    /**
     * Getter calculado para exponer el total directamente en el JSON.
     * Jackson lo serializa como un campo adicional llamado "total".
     */
    public BigDecimal getTotal() {
        return calcularTotal();
    }
}
