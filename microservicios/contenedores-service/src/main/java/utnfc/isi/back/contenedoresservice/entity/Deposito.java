package utnfc.isi.back.contenedoresservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/*
-- Tabla deposito
CREATE TABLE deposito (
    id_direccion BIGINT PRIMARY KEY,
    nombre VARCHAR(50),
    costo_estadia_diario DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_deposito_direccion
        FOREIGN KEY(id_direccion)
        REFERENCES direccion(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
 */

@Entity
@Table(name = "deposito")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@Getter @Setter
public class Deposito {

    // atributos
    @Id
    @Column(name = "id_direccion", nullable = false)
    private Long idDireccion;

    @Column(name = "nombre", length = 50)
    private String nombre;

    @Column(name = "costo_estadia_diario", nullable = false)
    private BigDecimal costoEstadiaDiario;

}
