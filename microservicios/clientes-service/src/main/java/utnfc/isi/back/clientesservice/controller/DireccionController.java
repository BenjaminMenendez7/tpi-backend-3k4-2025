package utnfc.isi.back.clientesservice.controller;

import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.clientesservice.entity.Direccion;
import utnfc.isi.back.clientesservice.service.DireccionService;

@RestController
@RequestMapping("/api/clientes")
public class DireccionController {

    private final DireccionService direccionService;

    public DireccionController(DireccionService direccionService) {
        this.direccionService = direccionService;
    }

    @PostMapping("/{idCliente}/direcciones")
    public Direccion crearDireccion(
            @PathVariable Long idCliente,
            @RequestBody Direccion direccion) {

        return direccionService.crearDireccionParaCliente(idCliente, direccion);
    }

}
