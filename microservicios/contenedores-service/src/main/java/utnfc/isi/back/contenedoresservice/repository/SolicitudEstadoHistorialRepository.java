package utnfc.isi.back.contenedoresservice.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import utnfc.isi.back.contenedoresservice.entity.SolicitudEstadoHistorial;

import java.util.List;

public interface SolicitudEstadoHistorialRepository extends JpaRepository<SolicitudEstadoHistorial, Long> {

    @EntityGraph(attributePaths = {"estadoSolicitud"})
    List<SolicitudEstadoHistorial> findByIdSolicitudOrderByFechaRegistroAsc(Long idSolicitud);
}
