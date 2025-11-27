package utnfc.isi.back.camionesservice.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
public class CamionDTO {
    private Long id;
    private Long idTipoCamion;
    private String patente;
    private BigDecimal capacidadPeso;
    private BigDecimal capacidadVolumen;
    private boolean disponible;
    private BigDecimal costoBaseKm;
    private BigDecimal consumoCombustiblePromedio;
    private Long idTransportista;
}

