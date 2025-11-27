package utnfc.isi.back.camionesservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfc.isi.back.camionesservice.entity.Tarifa;

import java.util.List;

public interface TarifaRepository extends JpaRepository<Tarifa, Long> {

    List<Tarifa> findByTipoCamionId(Long idTipoCamion);
}