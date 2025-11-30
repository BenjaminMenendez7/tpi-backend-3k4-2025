package utnfc.isi.back.clientesservice.controller;

import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.clientesservice.dto.*;
import utnfc.isi.back.clientesservice.entity.Cliente;
import utnfc.isi.back.clientesservice.service.ClienteService;

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

    // http://localhost:8081/api/clientes/1
    @GetMapping("/{id}")
    public ClienteDTO obtenerPorId(@PathVariable Long id) {
        return clienteService.buscarPorIdDTO(id);
    }

    // http://localhost:8081/api/clientes/documento/12345678
    @GetMapping("/documento/{documento}")
    public Cliente obtenerPorDocumento(@PathVariable String documento) {
        return clienteService.buscarPorDocumento(documento)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con documento: " + documento));
    }

    // http://localhost:8081/api/clientes/5/direcciones
    @GetMapping("/{id}/direcciones")
    public List<DireccionDTO> obtenerDireccionesCliente(@PathVariable Long id) {
        return clienteService.listarDireccionesCliente(id);
    }

}
