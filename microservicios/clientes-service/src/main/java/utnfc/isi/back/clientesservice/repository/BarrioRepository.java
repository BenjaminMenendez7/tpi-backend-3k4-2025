package utnfc.isi.back.clientesservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utnfc.isi.back.clientesservice.entity.Barrio;
import utnfc.isi.back.clientesservice.entity.Ciudad;

import java.util.Optional;

@Repository
public interface BarrioRepository extends JpaRepository<Barrio, Long> {
    Optional<Barrio> findByNombre(String nombre);
       Optional<Barrio> findByNombreAndCiudad(String nombre, Ciudad ciudad);

}
