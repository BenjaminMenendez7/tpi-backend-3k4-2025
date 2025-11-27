package utnfc.isi.back.contenedoresservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utnfc.isi.back.contenedoresservice.entity.Ruta;
import utnfc.isi.back.contenedoresservice.repository.RutaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RutaService {

    private final RutaRepository rutaRepository;

    public List<Ruta> findAll() {
        return rutaRepository.findAll();
    }

    public Ruta findById(Long id) {
        return rutaRepository.findById(id).orElse(null);
    }

    public Ruta save(Ruta ruta) {
        return rutaRepository.save(ruta);
    }

    public void delete(Long id) {
        rutaRepository.deleteById(id);
    }
}
