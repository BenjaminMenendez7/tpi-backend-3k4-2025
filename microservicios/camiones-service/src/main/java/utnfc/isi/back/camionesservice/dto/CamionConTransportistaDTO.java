package utnfc.isi.back.camionesservice.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
public class CamionConTransportistaDTO {
    private Long id;
    private String patente;
    private BigDecimal capacidadPeso;
    private BigDecimal capacidadVolumen;
    private boolean disponible;
    private BigDecimal costoBaseKm;
    private BigDecimal consumoCombustiblePromedio;

    private Long transportistaId;
    private String documento;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
}

