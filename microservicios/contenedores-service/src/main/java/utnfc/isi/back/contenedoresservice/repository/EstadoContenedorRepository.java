package utnfc.isi.back.contenedoresservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfc.isi.back.contenedoresservice.entity.EstadoContenedor;

import java.util.Optional;

public interface EstadoContenedorRepository extends JpaRepository<EstadoContenedor, Long> {
    Optional<EstadoContenedor> findByNombre(String nombre);
}
