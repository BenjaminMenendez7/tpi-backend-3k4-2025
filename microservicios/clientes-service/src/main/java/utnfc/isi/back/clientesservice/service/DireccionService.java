package utnfc.isi.back.clientesservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utnfc.isi.back.clientesservice.dto.DireccionDTO;
import utnfc.isi.back.clientesservice.entity.*;
import utnfc.isi.back.clientesservice.mapper.DireccionMapper;
import utnfc.isi.back.clientesservice.repository.*;
import utnfc.isi.back.common.exceptions.ResourceNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DireccionService {

    private final DireccionRepository direccionRepository;
    private final ClienteRepository clienteRepository;
    private final GeolocalizacionRepository geolocalizacionRepository;
    private final GeocodingService geocodingService;
    private final DireccionMapper direccionMapper;
    private final UbicacionService ubicacionService;
    private final CalleRepository calleRepository;

    public DireccionService(DireccionRepository direccionRepository,
                            ClienteRepository clienteRepository,
                            GeolocalizacionRepository geolocalizacionRepository,
                            GeocodingService geocodingService,
                            DireccionMapper direccionMapper,
                            UbicacionService ubicacionService,CalleRepository calleRepository) {
        this.direccionRepository = direccionRepository;
        this.clienteRepository = clienteRepository;
        this.geolocalizacionRepository = geolocalizacionRepository;
        this.geocodingService = geocodingService;
        this.direccionMapper = direccionMapper;
        this.ubicacionService = ubicacionService;
        this.calleRepository = calleRepository;
    }

    @Transactional
    public DireccionDTO crear(DireccionDTO dto, Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + clienteId));

        // Validaciones mínimas
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
        if (dto.getNroCalle() == null || dto.getNroCalle().isBlank()) {
            throw new IllegalArgumentException("El campo 'nroCalle' es obligatorio");
        }
        if (dto.getCodigoPostal() == null || dto.getCodigoPostal().isBlank()) {
            throw new IllegalArgumentException("El campo 'codigoPostal' es obligatorio");
        }

        Calle calle = (dto.getCalleId() != null)
                ? calleRepository.findById(dto.getCalleId())
                .orElseThrow(() -> new ResourceNotFoundException("Calle no encontrada con id: " + dto.getCalleId()))
                : ubicacionService.resolveCalle(dto);

        Direccion direccion = direccionMapper.toEntity(dto);
        direccion.setCliente(cliente);
        direccion.setCalle(calle);

        Direccion guardada = direccionRepository.save(direccion);

        String direccionCompleta = calle.getNombre() + " " + guardada.getNroCalle() + ", " + guardada.getCodigoPostal();
        try {
            GeocodingService.Coordenada coord = geocodingService.geocode(direccionCompleta).block();
            if (coord != null) {
                Geolocalizacion geo = new Geolocalizacion();
                geo.setDireccion(guardada);
                geo.setLatitud(BigDecimal.valueOf(coord.getLat()));
                geo.setLongitud(BigDecimal.valueOf(coord.getLon()));
                geolocalizacionRepository.save(geo);
                guardada.setGeolocalizacion(geo);
            }
        } catch (Exception e) {
        }

        return direccionMapper.toDTO(guardada);
    }


    public Optional<DireccionDTO> obtenerPorId(Long id) {
        return direccionRepository.findById(id)
                .map(direccionMapper::toDTO);
    }

    public List<DireccionDTO> listarPorCliente(Long clienteId) {
        return direccionRepository.findByClienteId(clienteId)
                .stream()
                .map(direccionMapper::toDTO)
                .toList();
    }

    @Transactional
    public void eliminar(Long id) {
        if (!direccionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dirección no encontrada con id: " + id);
        }
        direccionRepository.deleteById(id);
    }
}
