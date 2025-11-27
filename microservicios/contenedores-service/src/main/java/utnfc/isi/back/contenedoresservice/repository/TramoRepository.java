package utnfc.isi.back.contenedoresservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfc.isi.back.contenedoresservice.entity.Tramo;
import java.util.List;

public interface TramoRepository extends JpaRepository<Tramo, Long> {

    // Busca tramos asociados a una ruta
    List<Tramo> findByRuta_Id(Long rutaId);
    List<Tramo> findByRuta_IdOrderByIdAsc(Long idRuta);

}
