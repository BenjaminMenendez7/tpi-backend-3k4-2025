package utnfc.isi.back.clientesservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utnfc.isi.back.clientesservice.entity.Pais;
import utnfc.isi.back.clientesservice.entity.Provincia;

import java.util.Optional;

@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia, Long> {
    Optional<Provincia> findByNombre(String nombre);
      Optional<Provincia> findByNombreAndPais(String nombre, Pais pais);

}
