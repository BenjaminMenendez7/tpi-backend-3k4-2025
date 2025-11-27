package utnfc.isi.back.contenedoresservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "solicitud_estado_historial")
public class SolicitudEstadoHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_solicitud", nullable = false)
    private Long idSolicitud;

    @Column(name = "id_estado_solicitud", nullable = false)
    private Long idEstadoSolicitud;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;
}
