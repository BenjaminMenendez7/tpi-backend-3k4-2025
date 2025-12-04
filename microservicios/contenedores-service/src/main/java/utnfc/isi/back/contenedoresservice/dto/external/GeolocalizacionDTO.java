package utnfc.isi.back.contenedoresservice.dto.external;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeolocalizacionDTO {
    private Double latitud;
    private Double longitud;
}
