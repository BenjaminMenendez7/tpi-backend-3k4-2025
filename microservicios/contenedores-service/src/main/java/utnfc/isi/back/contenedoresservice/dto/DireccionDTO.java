package utnfc.isi.back.contenedoresservice.dto;

import lombok.Data;

@Data
public class DireccionDTO {

    private Long id;
    private String nombre;
    private String calle;
    private String ciudad;
    private String provincia;

    // Agregá lo que devuelva tu MS actual
}
