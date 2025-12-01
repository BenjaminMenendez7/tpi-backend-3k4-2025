package utnfc.isi.back.clientesservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "direccion")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@Getter @Setter
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nro_calle", length = 20, nullable = false)
    private String nroCalle;

    @Transient // ✅ no se persiste en la BD
    private String nombreCalle; // usado solo en DTOs o lógica de negocio

    @Column(name = "piso", length = 10)
    private String piso;

    @Column(name = "dpto", length = 10)
    private String dpto;

    @Column(name = "observaciones", length = 100)
    private String observaciones;

    @Column(name = "codigo_postal", length = 20)
    private String codigoPostal;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_calle", nullable = false)
    private Calle calle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @OneToOne(mappedBy = "direccion", cascade = CascadeType.ALL)
    private Geolocalizacion geolocalizacion;
}


