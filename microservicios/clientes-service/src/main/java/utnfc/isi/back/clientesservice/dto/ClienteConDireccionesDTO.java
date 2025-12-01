package utnfc.isi.back.clientesservice.dto;

import lombok.Data;
import java.util.List;

@Data
public class ClienteConDireccionesDTO {
    private Long id;
    private String documento;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;

    private List<DireccionDTO> direcciones;
}
