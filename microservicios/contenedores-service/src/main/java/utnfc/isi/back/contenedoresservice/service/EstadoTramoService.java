package utnfc.isi.back.contenedoresservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utnfc.isi.back.contenedoresservice.entity.EstadoTramo;
import utnfc.isi.back.contenedoresservice.repository.EstadoTramoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadoTramoService {

    private final EstadoTramoRepository estadoTramoRepository;

    public List<EstadoTramo> findAll() {
        return estadoTramoRepository.findAll();
    }

    public EstadoTramo findById(Long id) {
        return estadoTramoRepository.findById(id).orElse(null);
    }

    public EstadoTramo save(EstadoTramo estadoTramo) {
        return estadoTramoRepository.save(estadoTramo);
    }

    public void delete(Long id) {
        estadoTramoRepository.deleteById(id);
    }
}
