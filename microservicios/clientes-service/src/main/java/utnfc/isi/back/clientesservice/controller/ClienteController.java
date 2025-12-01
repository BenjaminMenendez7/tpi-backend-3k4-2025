package utnfc.isi.back.clientesservice.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.clientesservice.dto.ClienteConDireccionesDTO;
import utnfc.isi.back.clientesservice.dto.ClienteDTO;
import utnfc.isi.back.clientesservice.dto.DireccionDTO;
import utnfc.isi.back.clientesservice.entity.Cliente;
import utnfc.isi.back.clientesservice.mapper.ClienteMapper;
import utnfc.isi.back.clientesservice.service.ClienteService;
import utnfc.isi.back.common.exceptions.ResourceNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    public ClienteController(ClienteService clienteService, ClienteMapper clienteMapper) {
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
    }

    @GetMapping
    public List<ClienteDTO> listar() {
        return clienteService.listarClientesDTO();
    }

    @PostMapping
    public ClienteConDireccionesDTO crear(@Valid @RequestBody ClienteConDireccionesDTO clienteDTO) {
        Cliente cliente = clienteMapper.toEntityConDirecciones(clienteDTO);
        Cliente clienteGuardado = clienteService.crearClienteConDirecciones(cliente);
        return clienteMapper.toConDireccionesDTO(clienteGuardado);
    }

    @GetMapping("/{id}")
    public ClienteDTO obtenerPorId(@PathVariable Long id) {
        ClienteDTO cliente = clienteService.buscarPorIdDTO(id);
        if (cliente == null) {
            throw new ResourceNotFoundException("Cliente no encontrado con id: " + id);
        }
        return cliente;
    }

    @GetMapping("/documento/{documento}")
    public Cliente obtenerPorDocumento(@PathVariable String documento) {
        return clienteService.buscarPorDocumento(documento)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente no encontrado con documento: " + documento
                ));
    }

    @GetMapping("/{id}/direcciones")
    public List<DireccionDTO> obtenerDireccionesCliente(@PathVariable Long id) {
        // ✅ La condición estaba invertida
        if (clienteService.buscarPorId(id).isEmpty()) {
            throw new ResourceNotFoundException("Cliente no encontrado con id: " + id);
        }
        return clienteService.listarDireccionesCliente(id);
    }
}
