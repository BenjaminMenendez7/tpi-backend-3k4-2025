package utnfc.isi.back.clientesservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/*
-- Tabla geolocalizacion
CREATE TABLE geolocalizacion (
    id_direccion BIGINT PRIMARY KEY,
    latitud DECIMAL(9,6) NOT NULL,
    longitud DECIMAL(9,6) NOT NULL,
    CONSTRAINT fk_geolocalizacion_direccion
        FOREIGN KEY(id_direccion)
        REFERENCES direccion(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
 */

@Entity
@Table(name = "geolocalizacion")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@Getter @Setter
public class Geolocalizacion {

    //atributo
    @Id
    @Column(name = "id_direccion", nullable = false)
    private Long idDireccion;      // PK = FK, no se genera

    @OneToOne
    @MapsId                       // Usa el mismo id que la dirección
    @JoinColumn(name = "id_direccion")
    private Direccion direccion;

    @Column(name = "latitud", precision = 9, scale = 6, nullable = false)
    private BigDecimal latitud;

    @Column(name = "longitud", precision = 9, scale = 6, nullable = false)
    private BigDecimal longitud;

}
