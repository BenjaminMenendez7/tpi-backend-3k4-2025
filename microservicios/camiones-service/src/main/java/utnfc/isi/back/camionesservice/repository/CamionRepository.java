package utnfc.isi.back.camionesservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfc.isi.back.camionesservice.entity.Camion;

import java.util.List;
import java.util.Optional;

public interface CamionRepository extends JpaRepository<Camion, Long> {

    // Buscar por patente (para evitar duplicados y validar)
    Optional<Camion> findByPatente(String patente);

    // Camiones disponibles
    List<Camion> findByDisponibleTrue();

    // Camiones por transportista
    List<Camion> findByTransportistaId(Long idTransportista);

}
