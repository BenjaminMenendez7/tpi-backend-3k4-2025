package utnfc.isi.back.contenedoresservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfc.isi.back.contenedoresservice.entity.Solicitud;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
}

