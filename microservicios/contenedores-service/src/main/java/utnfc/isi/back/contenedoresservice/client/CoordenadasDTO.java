package utnfc.isi.back.contenedoresservice.client;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CoordenadasDTO {
    private BigDecimal latitud;
    private BigDecimal longitud;
}
