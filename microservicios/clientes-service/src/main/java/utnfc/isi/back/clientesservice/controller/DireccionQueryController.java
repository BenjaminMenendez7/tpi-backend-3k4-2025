package utnfc.isi.back.clientesservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utnfc.isi.back.clientesservice.dto.DireccionDTO;
import utnfc.isi.back.clientesservice.entity.Direccion;
import utnfc.isi.back.clientesservice.mapper.DireccionMapper;
import utnfc.isi.back.clientesservice.service.DireccionService;
import utnfc.isi.back.common.exceptions.ResourceNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/direcciones")
public class DireccionQueryController {

    private final DireccionService direccionService;

    public DireccionQueryController(DireccionService direccionService) {
        this.direccionService = direccionService;
    }

    @GetMapping("/{id}")
    public DireccionDTO obtenerPorId(@PathVariable Long id) {
        return direccionService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dirección no encontrada con id: " + id));
    }

    @GetMapping("/cliente/{clienteId}")
    public List<DireccionDTO> listarPorCliente(@PathVariable Long clienteId) {
        return direccionService.listarPorCliente(clienteId);
    }
}

