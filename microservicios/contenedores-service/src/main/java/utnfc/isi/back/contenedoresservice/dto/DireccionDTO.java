package utnfc.isi.back.contenedoresservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DireccionDTO {
    private Long id;
    private String nombre;
    private String calle;
    private String ciudad;
    private String provincia;
}