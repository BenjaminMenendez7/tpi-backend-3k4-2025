package utnfc.isi.back.clientesservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utnfc.isi.back.clientesservice.entity.Geolocalizacion;

import java.util.Optional;

@Repository
public interface GeolocalizacionRepository extends JpaRepository<Geolocalizacion, Long> {
    Optional<Geolocalizacion> findByDireccionId(Long direccionId);
}
