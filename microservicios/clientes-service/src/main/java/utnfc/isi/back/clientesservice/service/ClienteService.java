package utnfc.isi.back.clientesservice.service;

import org.springframework.stereotype.Service;
import utnfc.isi.back.clientesservice.dto.*;
import utnfc.isi.back.clientesservice.entity.Cliente;
import utnfc.isi.back.clientesservice.mapper.*;
import utnfc.isi.back.clientesservice.repository.ClienteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    // atributo
    private final ClienteRepository clienteRepository;

    // constructor
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // MÉTODOS
    // crear
    public Cliente crearCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // Listar todos
    public List<ClienteDTO> listarClientesDTO() {
        return clienteRepository.findAll()
                .stream()
                .map(ClienteMapper::toDTO)
                .toList();
    }

    public ClienteDTO buscarPorIdDTO(Long id) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No encontrado"));

        return ClienteMapper.toDTO(c);
    }


    // Buscar uno por id
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    // Buscar por documento (no existe DNI en la entidad)
    public Optional<Cliente> buscarPorDocumento(String documento) {
        return clienteRepository.findByDocumento(documento);
    }

    // Listar direcciones del cliente
    public List<DireccionDTO> listarDireccionesCliente(Long clienteId) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + clienteId));

        return cliente.getDirecciones()
                .stream()
                .map(DireccionMapper::toDTO)
                .toList();
    }


    // Actualizar
    public Cliente actualizarCliente(Long id, Cliente datos) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));

        existente.setNombre(datos.getNombre());
        existente.setApellido(datos.getApellido());
        existente.setDocumento(datos.getDocumento());
        existente.setEmail(datos.getEmail());
        existente.setTelefono(datos.getTelefono());
        existente.setDirecciones(datos.getDirecciones());

        return clienteRepository.save(existente);
    }

    // Eliminar
    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado con id: " + id);
        }
        clienteRepository.deleteById(id);
    }




}
