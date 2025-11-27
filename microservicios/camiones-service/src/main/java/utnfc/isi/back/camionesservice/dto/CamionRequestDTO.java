package utnfc.isi.back.camionesservice.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
public class CamionRequestDTO {
    private String patente;
    private BigDecimal capacidadPeso;
    private BigDecimal capacidadVolumen;
    private BigDecimal costoBaseKm;
    private BigDecimal consumoCombustiblePromedio;
    private Boolean disponible;
    private Long idTransportista;
}

