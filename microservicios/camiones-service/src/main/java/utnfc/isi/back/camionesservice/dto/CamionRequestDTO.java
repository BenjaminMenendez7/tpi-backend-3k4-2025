package utnfc.isi.back.camionesservice.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter @Setter
public class CamionRequestDTO {
    private String patente;
    private BigDecimal capacidadPeso;
    private BigDecimal capacidadVolumen;
    private BigDecimal costoBaseKm;
    private Long idTransportista;
    private Long idTipoCamion; // ➜ faltaba este campo
}
