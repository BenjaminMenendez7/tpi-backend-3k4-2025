package utnfc.isi.back.contenedoresservice.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import utnfc.isi.back.contenedoresservice.entity.Solicitud;
import java.util.Optional;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    @EntityGraph(attributePaths = {
            "contenedor",
            "estadoSolicitud",
            "rutas",
            "rutas.tramos",
            "rutas.tramos.estadoTramo"
    })
    Optional<Solicitud> findWithGraphById(Long id);
}