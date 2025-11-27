package utnfc.isi.back.contenedoresservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utnfc.isi.back.contenedoresservice.entity.Deposito;
import utnfc.isi.back.contenedoresservice.repository.DepositoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepositoService {

    private final DepositoRepository depositoRepository;

    public List<Deposito> findAll() {
        return depositoRepository.findAll();
    }

    public Deposito findById(Long idDireccion) {
        return depositoRepository.findById(idDireccion).orElse(null);
    }

    public Deposito save(Deposito deposito) {
        return depositoRepository.save(deposito);
    }

    public void delete(Long idDireccion) {
        depositoRepository.deleteById(idDireccion);
    }
}
