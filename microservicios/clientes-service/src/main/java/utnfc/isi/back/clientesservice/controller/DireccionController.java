package utnfc.isi.back.clientesservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.clientesservice.dto.DireccionDTO;
import utnfc.isi.back.clientesservice.entity.Direccion;
import utnfc.isi.back.clientesservice.service.DireccionService;

@RestController
@RequestMapping("/clientes")
public class DireccionController {

    private final DireccionService direccionService;

    public DireccionController(DireccionService direccionService) {
        this.direccionService = direccionService;
    }

    @PostMapping("/clientes/{clienteId}/direcciones")
    public ResponseEntity<DireccionDTO> crearDireccion(
            @PathVariable Long clienteId,
            @RequestBody DireccionDTO dto) {

        DireccionDTO guardada = direccionService.crear(dto, clienteId);
        return ResponseEntity.ok(guardada);
    }

}
