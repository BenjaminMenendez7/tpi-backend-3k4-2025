package utnfc.isi.back.camionesservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfc.isi.back.camionesservice.entity.TipoCamion;

public interface TipoCamionRepository extends JpaRepository<TipoCamion, Long> {
}
