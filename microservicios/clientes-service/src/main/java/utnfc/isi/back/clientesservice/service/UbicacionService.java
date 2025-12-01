package utnfc.isi.back.clientesservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utnfc.isi.back.clientesservice.dto.DireccionDTO;
import utnfc.isi.back.clientesservice.entity.*;
import utnfc.isi.back.clientesservice.repository.*;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UbicacionService {

    private final PaisRepository paisRepository;
    private final ProvinciaRepository provinciaRepository;
    private final CiudadRepository ciudadRepository;
    private final BarrioRepository barrioRepository;
    private final CalleRepository calleRepository;

    public UbicacionService(PaisRepository paisRepository,
                            ProvinciaRepository provinciaRepository,
                            CiudadRepository ciudadRepository,
                            BarrioRepository barrioRepository,
                            CalleRepository calleRepository) {
        this.paisRepository = paisRepository;
        this.provinciaRepository = provinciaRepository;
        this.ciudadRepository = ciudadRepository;
        this.barrioRepository = barrioRepository;
        this.calleRepository = calleRepository;
    }

    @Transactional
    public Calle resolveCalle(DireccionDTO dto) {
        validarCampos(dto);

        Pais pais = paisRepository.findByNombre(dto.getPais())
                .orElseGet(() -> {
                    Pais nuevo = new Pais();
                    nuevo.setNombre(dto.getPais());
                    return paisRepository.save(nuevo);
                });

        Provincia provincia = provinciaRepository.findByNombreAndPais(dto.getProvincia(), pais)
                .orElseGet(() -> {
                    Provincia nueva = new Provincia();
                    nueva.setNombre(dto.getProvincia());
                    nueva.setPais(pais);
                    return provinciaRepository.save(nueva);
                });

        Ciudad ciudad = ciudadRepository.findByNombreAndProvincia(dto.getCiudad(), provincia)
                .orElseGet(() -> {
                    Ciudad nueva = new Ciudad();
                    nueva.setNombre(dto.getCiudad());
                    nueva.setProvincia(provincia);
                    return ciudadRepository.save(nueva);
                });

        Barrio barrio = barrioRepository.findByNombreAndCiudad(dto.getBarrio(), ciudad)
                .orElseGet(() -> {
                    Barrio nuevo = new Barrio();
                    nuevo.setNombre(dto.getBarrio());
                    nuevo.setCiudad(ciudad);
                    return barrioRepository.save(nuevo);
                });

        return calleRepository.findByNombreAndBarrio(dto.getNombreCalle(), barrio)
                .orElseGet(() -> {
                    Calle nueva = new Calle();
                    nueva.setNombre(dto.getNombreCalle());
                    nueva.setBarrio(barrio);
                    return calleRepository.save(nueva);
                });
    }

    private void validarCampos(DireccionDTO dto) {
        if (dto.getPais() == null || dto.getPais().isBlank()) {
            throw new IllegalArgumentException("El campo 'pais' es obligatorio");
        }
        if (dto.getProvincia() == null || dto.getProvincia().isBlank()) {
            throw new IllegalArgumentException("El campo 'provincia' es obligatorio");
        }
        if (dto.getCiudad() == null || dto.getCiudad().isBlank()) {
            throw new IllegalArgumentException("El campo 'ciudad' es obligatorio");
        }
        if (dto.getBarrio() == null || dto.getBarrio().isBlank()) {
            throw new IllegalArgumentException("El campo 'barrio' es obligatorio");
        }
        if (dto.getNombreCalle() == null || dto.getNombreCalle().isBlank()) {
            throw new IllegalArgumentException("El campo 'nombreCalle' es obligatorio");
        }
    }

    @Transactional
    public Calle resolveCalleByNames(String paisNombre, String provinciaNombre, String ciudadNombre,
                                     String barrioNombre, String calleNombre) {

        Pais pais = paisRepository.findByNombre(paisNombre)
                .orElseGet(() -> {
                    Pais nuevo = new Pais();
                    nuevo.setNombre(paisNombre != null ? paisNombre : "DESCONOCIDO");
                    return paisRepository.save(nuevo);
                });

        Provincia provincia = provinciaRepository.findByNombre(provinciaNombre)
                .orElseGet(() -> {
                    Provincia nueva = new Provincia();
                    nueva.setNombre(provinciaNombre != null ? provinciaNombre : "DESCONOCIDO");
                    nueva.setPais(pais);
                    return provinciaRepository.save(nueva);
                });

        Ciudad ciudad = ciudadRepository.findByNombre(ciudadNombre)
                .orElseGet(() -> {
                    Ciudad nueva = new Ciudad();
                    nueva.setNombre(ciudadNombre != null ? ciudadNombre : "DESCONOCIDO");
                    nueva.setProvincia(provincia);
                    return ciudadRepository.save(nueva);
                });

        Barrio barrio = barrioRepository.findByNombre(barrioNombre)
                .orElseGet(() -> {
                    Barrio nuevo = new Barrio();
                    nuevo.setNombre(barrioNombre != null ? barrioNombre : "DESCONOCIDO");
                    nuevo.setCiudad(ciudad);
                    return barrioRepository.save(nuevo);
                });

        return calleRepository.findByNombre(calleNombre)
                .orElseGet(() -> {
                    Calle nueva = new Calle();
                    nueva.setNombre(calleNombre != null ? calleNombre : "DESCONOCIDO");
                    nueva.setBarrio(barrio);
                    return calleRepository.save(nueva);
                });
    }
}