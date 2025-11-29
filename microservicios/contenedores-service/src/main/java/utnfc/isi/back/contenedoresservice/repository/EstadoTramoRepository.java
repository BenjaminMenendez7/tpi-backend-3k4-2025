package utnfc.isi.back.contenedoresservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfc.isi.back.contenedoresservice.entity.EstadoTramo;

import java.util.Optional;

public interface EstadoTramoRepository extends JpaRepository<EstadoTramo, Long> {
    Optional<EstadoTramo> findByNombre(String name);
}