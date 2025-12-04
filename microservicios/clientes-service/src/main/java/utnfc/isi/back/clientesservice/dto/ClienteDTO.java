package utnfc.isi.back.clientesservice.dto;

import lombok.*;

@Getter @Setter
public class ClienteDTO {
    private Long id;
    private String documento;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    // getters y setters
}
