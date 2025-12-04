package utnfc.isi.back.contenedoresservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfc.isi.back.contenedoresservice.entity.Contenedor;

import java.util.Optional;

public interface ContenedorRepository extends JpaRepository<Contenedor, Long> {
    Optional<Contenedor> findById(Long idContenedor);
}
