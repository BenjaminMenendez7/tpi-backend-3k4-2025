package utnfc.isi.back.contenedoresservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfc.isi.back.contenedoresservice.entity.Ruta;
import java.util.List;

public interface RutaRepository extends JpaRepository<Ruta, Long> {

    // Busca rutas asociadas a una solicitud
    List<Ruta> findBySolicitud_Id(Long solicitudId);
}
