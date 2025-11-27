package utnfc.isi.back.camionesservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfc.isi.back.camionesservice.entity.Transportista;

public interface TransportistaRepository extends JpaRepository<Transportista, Long> {

    // búsquedas útiles desde el service
    boolean existsByDocumento(String documento);

    boolean existsByEmail(String email);
}
