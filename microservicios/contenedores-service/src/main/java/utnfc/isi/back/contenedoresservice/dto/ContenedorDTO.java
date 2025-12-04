package utnfc.isi.back.contenedoresservice.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContenedorDTO {

    private Long id;
    private BigDecimal peso;
    private BigDecimal volumen;

    private Long idCliente;

    // nombre del estado (dato útil para mostrar)
    private String estado;
}


