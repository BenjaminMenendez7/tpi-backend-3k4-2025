package utnfc.isi.back.contenedoresservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorialEstadoDTO {
    private String estado;
    private LocalDateTime fecha;
}
