package utnfc.isi.back.clientesservice.controller;

import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.clientesservice.dto.ClienteDTO;
import utnfc.isi.back.clientesservice.dto.DireccionDTO;
import utnfc.isi.back.clientesservice.entity.Cliente;
import utnfc.isi.back.clientesservice.service.ClienteService;
import utnfc.isi.back.common.exceptions.ResourceNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<ClienteDTO> listar() {
        return clienteService.listarClientesDTO();
    }

    @PostMapping
    public Cliente crear(@RequestBody Cliente cliente) {
        return clienteService.crearCliente(cliente);
    }

    // Buscar cliente por ID
    @GetMapping("/{id}")
    public ClienteDTO obtenerPorId(@PathVariable Long id) {
        ClienteDTO cliente = clienteService.buscarPorIdDTO(id);
        if (cliente == null) {
            throw new ResourceNotFoundException("Cliente no encontrado con id: " + id);
        }
        return cliente;
    }

    // Buscar cliente por documento
    @GetMapping("/documento/{documento}")
    public Cliente obtenerPorDocumento(@PathVariable String documento) {
        return clienteService.buscarPorDocumento(documento)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente no encontrado con documento: " + documento
                ));
    }

    // Listar direcciones de un cliente
    @GetMapping("/{id}/direcciones")
    public List<DireccionDTO> obtenerDireccionesCliente(@PathVariable Long id) {
        if (clienteService.buscarPorId(id).isPresent()) {
            throw new ResourceNotFoundException("Cliente no encontrado con id: " + id);
        }
        return clienteService.listarDireccionesCliente(id);
    }
}
