package utnfc.isi.back.clientesservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfc.isi.back.clientesservice.entity.Geolocalizacion;

public interface GeolocalizacionRepository extends JpaRepository<Geolocalizacion, Long> {

}
