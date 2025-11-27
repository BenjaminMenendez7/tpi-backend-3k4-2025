package utnfc.isi.back.camionesservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarifaDTO {

    private BigDecimal costoKm;
    private BigDecimal costoCombustible;
    private BigDecimal costoEstadiaDeposito;
}
