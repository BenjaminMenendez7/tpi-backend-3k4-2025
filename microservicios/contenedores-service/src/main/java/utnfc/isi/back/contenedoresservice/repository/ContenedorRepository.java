package utnfc.isi.back.contenedoresservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfc.isi.back.contenedoresservice.entity.Contenedor;

public interface ContenedorRepository extends JpaRepository<Contenedor, Long> {
}
