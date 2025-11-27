package utnfc.isi.back.clientesservice.service;

import org.springframework.stereotype.Service;
import utnfc.isi.back.clientesservice.entity.Direccion;
import utnfc.isi.back.clientesservice.entity.Geolocalizacion;
import utnfc.isi.back.clientesservice.repository.GeolocalizacionRepository;

import java.math.BigDecimal;

@Service
public class GeolocalizacionService {

    // atributo
    private final GeolocalizacionRepository geolocalizacionRepository;

    // constructor
    public GeolocalizacionService(GeolocalizacionRepository geolocalizacionRepository) {
        this.geolocalizacionRepository = geolocalizacionRepository;
    }

    // métodos
    public Geolocalizacion guardar(Geolocalizacion geolocalizacion) {
        return geolocalizacionRepository.save(geolocalizacion);
    }

    public Geolocalizacion generarParaDireccion(Direccion direccion) {

        // Por ahora generamos coordenadas ficticias o calculadas simple
        // Más adelante esto se reemplazará por OSRM o Google Maps

        Geolocalizacion geo = new Geolocalizacion();

        // EJEMPLO: poner coordenadas dummy
        geo.setLatitud(new BigDecimal("-34.603722"));   // latitud de ejemplo
        geo.setLongitud(new BigDecimal("-58.381592"));  // longitud de ejemplo

        // Asociar la dirección (FK)
        geo.setDireccion(direccion);

        return geo;
    }

    public Geolocalizacion obtenerPorIdDireccion(Long idDireccion) {
        return geolocalizacionRepository.findById(idDireccion).orElse(null);
    }


}
