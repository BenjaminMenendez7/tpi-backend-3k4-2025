package utnfc.isi.back.clientesservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utnfc.isi.back.clientesservice.dto.ClienteDTO;
import utnfc.isi.back.clientesservice.dto.DireccionDTO;
import utnfc.isi.back.clientesservice.entity.*;
import utnfc.isi.back.clientesservice.mapper.ClienteMapper;
import utnfc.isi.back.clientesservice.mapper.DireccionMapper;
import utnfc.isi.back.clientesservice.repository.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final DireccionRepository direccionRepository;
    private final GeolocalizacionRepository geolocalizacionRepository;
    private final GeocodingService geocodingService;
    private final ClienteMapper clienteMapper;
    private final DireccionMapper direccionMapper;
    private final UbicacionService ubicacionService;

    public ClienteService(ClienteRepository clienteRepository,
                          DireccionRepository direccionRepository,
                          GeolocalizacionRepository geolocalizacionRepository,
                          GeocodingService geocodingService,
                          ClienteMapper clienteMapper,
                          DireccionMapper direccionMapper,
                          UbicacionService ubicacionService) {
        this.clienteRepository = clienteRepository;
        this.direccionRepository = direccionRepository;
        this.geolocalizacionRepository = geolocalizacionRepository;
        this.geocodingService = geocodingService;
        this.clienteMapper = clienteMapper;
        this.direccionMapper = direccionMapper;
        this.ubicacionService = ubicacionService;
    }

    @Transactional
    public Cliente crearClienteConDirecciones(Cliente cliente) {
        if (cliente.getDirecciones() != null && !cliente.getDirecciones().isEmpty()) {
            for (Direccion direccion : cliente.getDirecciones()) {
                DireccionDTO dto = direccionMapper.toDTO(direccion);

                // Normalizar valores nulos → reemplazar por "DESCONOCIDO"
                String pais = (dto.getPais() == null || dto.getPais().isBlank()) ? "DESCONOCIDO" : dto.getPais();
                String provincia = (dto.getProvincia() == null || dto.getProvincia().isBlank()) ? "DESCONOCIDO" : dto.getProvincia();
                String ciudad = (dto.getCiudad() == null || dto.getCiudad().isBlank()) ? "DESCONOCIDO" : dto.getCiudad();
                String barrio = (dto.getBarrio() == null || dto.getBarrio().isBlank()) ? "DESCONOCIDO" : dto.getBarrio();
                String calleNombre = (dto.getNombreCalle() == null || dto.getNombreCalle().isBlank()) ? "DESCONOCIDO" : dto.getNombreCalle();

                Calle calle = ubicacionService.resolveCalleByNames(pais, provincia, ciudad, barrio, calleNombre);

                direccion.setCalle(calle);
                direccion.setCliente(cliente);
            }
        }

        Cliente clienteGuardado = clienteRepository.save(cliente);

        if (clienteGuardado.getDirecciones() != null) {
            for (Direccion direccion : clienteGuardado.getDirecciones()) {
                Direccion dirGuardada = direccionRepository.save(direccion);

                String direccionCompleta = direccion.getCalle().getNombre() + " " +
                        (direccion.getNroCalle() != null ? direccion.getNroCalle() : "") + ", " +
                        (direccion.getCodigoPostal() != null ? direccion.getCodigoPostal() : "");

                try {
                    GeocodingService.Coordenada coord = geocodingService.geocode(direccionCompleta).block();
                    if (coord != null) {
                        Geolocalizacion geo = new Geolocalizacion();
                        geo.setDireccion(dirGuardada);
                        geo.setLatitud(BigDecimal.valueOf(coord.getLat()));
                        geo.setLongitud(BigDecimal.valueOf(coord.getLon()));
                        geolocalizacionRepository.save(geo);
                    }
                } catch (Exception e) {
                }
            }
        }

        return clienteGuardado;
    }

    public List<ClienteDTO> listarClientesDTO() {
        return clienteRepository.findAll()
                .stream()
                .map(clienteMapper::toDTO)
                .toList();
    }

    public ClienteDTO buscarPorIdDTO(Long id) {
        return clienteRepository.findById(id)
                .map(clienteMapper::toDTO)
                .orElse(null);
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> buscarPorDocumento(String documento) {
        return clienteRepository.findByDocumento(documento);
    }

    public List<DireccionDTO> listarDireccionesCliente(Long idCliente) {
        return direccionRepository.findByClienteId(idCliente)
                .stream()
                .map(direccionMapper::toDTO)
                .toList();
    }
}
