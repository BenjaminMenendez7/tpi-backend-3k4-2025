package utnfc.isi.back.contenedoresservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HistorialEstadoDTO {
    private String estado;
    private LocalDateTime fecha;
}
