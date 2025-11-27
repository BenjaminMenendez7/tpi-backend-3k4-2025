package utnfc.isi.back.contenedoresservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utnfc.isi.back.contenedoresservice.entity.TipoTramo;
import utnfc.isi.back.contenedoresservice.repository.TipoTramoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoTramoService {

    private final TipoTramoRepository tipoTramoRepository;

    public List<TipoTramo> findAll() {
        return tipoTramoRepository.findAll();
    }

    public TipoTramo findById(Long id) {
        return tipoTramoRepository.findById(id).orElse(null);
    }

    public TipoTramo save(TipoTramo tipoTramo) {
        return tipoTramoRepository.save(tipoTramo);
    }

    public void delete(Long id) {
        tipoTramoRepository.deleteById(id);
    }
}
