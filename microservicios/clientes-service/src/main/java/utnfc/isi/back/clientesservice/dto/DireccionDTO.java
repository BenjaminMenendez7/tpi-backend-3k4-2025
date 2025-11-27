package utnfc.isi.back.clientesservice.dto;

import lombok.*;

@Getter @Setter
public class DireccionDTO {

    private Long id;
    private String nroCalle;
    private String piso;
    private String dpto;
    private String observaciones;
    private String codigoPostal;

    private Long calleId; // desde ManyToOne Calle
    private String nombreCalle;
    private Long clienteId;   // desde ManyToOne Cliente

}
