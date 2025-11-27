package utnfc.isi.back.contenedoresservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utnfc.isi.back.contenedoresservice.entity.Contenedor;
import utnfc.isi.back.contenedoresservice.repository.ContenedorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContenedorService {

    private final ContenedorRepository contenedorRepository;

    public List<Contenedor> findAll() {
        return contenedorRepository.findAll();
    }

    public Contenedor findById(Long id) {
        return contenedorRepository.findById(id).orElse(null);
    }

    public Contenedor save(Contenedor contenedor) {
        return contenedorRepository.save(contenedor);
    }

    public void delete(Long id) {
        contenedorRepository.deleteById(id);
    }
}
