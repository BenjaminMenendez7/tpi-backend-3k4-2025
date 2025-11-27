package utnfc.isi.back.contenedoresservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utnfc.isi.back.contenedoresservice.entity.Deposito;

public interface DepositoRepository extends JpaRepository<Deposito, Long> {
}
