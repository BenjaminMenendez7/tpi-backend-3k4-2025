package utnfc.isi.back.contenedoresservice.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
public class DepositoDTO {

    private Long idDireccion;
    private String nombre;
    private BigDecimal costoEstadiaDiario;
}
