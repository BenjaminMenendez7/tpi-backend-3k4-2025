package utnfc.isi.back.clientesservice.service;

import org.springframework.stereotype.Service;
import utnfc.isi.back.clientesservice.entity.Cliente;
import utnfc.isi.back.clientesservice.entity.Direccion;
import utnfc.isi.back.clientesservice.entity.Geolocalizacion;
import utnfc.isi.back.clientesservice.repository.ClienteRepository;
import utnfc.isi.back.clientesservice.repository.DireccionRepository;
import utnfc.isi.back.clientesservice.repository.GeolocalizacionRepository;

import java.math.BigDecimal;

@Service
public class DireccionService {

    // atributo
    private final DireccionRepository direccionRepository;
    private final ClienteRepository clienteRepository;
    private final GeolocalizacionRepository geolocalizacionRepository;
    private final GeolocalizacionService geolocalizacionService;

    // constructor
    public DireccionService(DireccionRepository direccionRepository,
                            ClienteRepository clienteRepository,
                            GeolocalizacionRepository geolocalizacionRepository,
                            GeolocalizacionService geolocalizacionService) {
        this.direccionRepository = direccionRepository;
        this.clienteRepository = clienteRepository;
        this.geolocalizacionRepository = geolocalizacionRepository;
        this.geolocalizacionService = geolocalizacionService;
    }


    // métodos
    public Direccion guardar(Direccion direccion) {
        return direccionRepository.save(direccion);
    }

    public Direccion guardarSimple(Direccion direccion) {
        return direccionRepository.save(direccion);
    }

    public Direccion guardarConGeolocalizacion(Direccion direccion) {
        Direccion dirGuardada = direccionRepository.save(direccion);

        Geolocalizacion geo = new Geolocalizacion();
        geo.setDireccion(dirGuardada);

        geo.setLatitud(BigDecimal.valueOf(-34.6037));    // CABA, por ahora ficticio
        geo.setLongitud(BigDecimal.valueOf(-58.3816));   // CABA, por ahora ficticio

        geolocalizacionRepository.save(geo);

        return dirGuardada;
    }

    public Direccion crearDireccionParaCliente(Long idCliente, Direccion direccion) {

        // 1. Buscar cliente
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // 2. Asignar el cliente a la dirección
        direccion.setCliente(cliente);

        // 3. Guardar la dirección
        Direccion direccionGuardada = direccionRepository.save(direccion);

        // 4. Generar la geolocalización (lat/lon)
        Geolocalizacion geo = geolocalizacionService.generarParaDireccion(direccionGuardada);

        // 5. Asignar la dirección en la geolocalización
        geo.setDireccion(direccionGuardada);

        // 6. Guardar geolocalización
        geolocalizacionRepository.save(geo);

        return direccionGuardada;
    }

    public Direccion obtenerPorId(Long id) {
        return direccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));
    }





}
