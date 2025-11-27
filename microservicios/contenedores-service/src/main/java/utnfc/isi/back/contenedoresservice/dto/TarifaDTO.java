package utnfc.isi.back.contenedoresservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TarifaDTO {
    private BigDecimal costoKm;
    private BigDecimal costoCombustible;
    private BigDecimal costoEstadiaDeposito;
}
