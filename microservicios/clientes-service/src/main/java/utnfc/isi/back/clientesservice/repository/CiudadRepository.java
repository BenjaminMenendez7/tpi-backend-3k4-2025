package utnfc.isi.back.clientesservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utnfc.isi.back.clientesservice.entity.Ciudad;
import utnfc.isi.back.clientesservice.entity.Provincia;

import java.util.Optional;

@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
    Optional<Ciudad> findByNombre(String nombre);

       Optional<Ciudad> findByNombreAndProvincia(String nombre, Provincia provincia);


}
