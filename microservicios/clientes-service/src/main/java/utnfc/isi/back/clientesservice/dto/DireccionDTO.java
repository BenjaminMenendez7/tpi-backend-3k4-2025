package utnfc.isi.back.clientesservice.dto;

import lombok.Data;

@Data
public class DireccionDTO {
    private Long id;
    private String nroCalle;
    private String piso;
    private String dpto;
    private String observaciones;
    private String codigoPostal;

    private Long calleId;       // FK hacia la entidad Calle
    private String nombreCalle; // nombre de la calle, solo para mostrar

    private Long clienteId;

    // Jerarquía de ubicación
    private String barrio;
    private String ciudad;
    private String provincia;
    private String pais;
}
