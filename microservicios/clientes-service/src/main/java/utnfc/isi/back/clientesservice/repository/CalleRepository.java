package utnfc.isi.back.clientesservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utnfc.isi.back.clientesservice.entity.Barrio;
import utnfc.isi.back.clientesservice.entity.Calle;

import java.util.Optional;

@Repository
public interface CalleRepository extends JpaRepository<Calle, Long> {
    Optional<Calle> findByNombre(String nombre);
    Optional<Calle> findByNombreAndBarrio(String nombre, Barrio barrio);
}
