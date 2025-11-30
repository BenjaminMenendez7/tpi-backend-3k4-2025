package utnfc.isi.back.clientesservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utnfc.isi.back.clientesservice.dto.DireccionDTO;
import utnfc.isi.back.clientesservice.entity.Direccion;
import utnfc.isi.back.clientesservice.mapper.DireccionMapper;
import utnfc.isi.back.clientesservice.service.DireccionService;

@RestController
@RequestMapping("/direcciones")
public class DireccionQueryController {

    private final DireccionService direccionService;
    private final DireccionMapper direccionMapper;

    public DireccionQueryController(DireccionService direccionService,
                                    DireccionMapper direccionMapper) {
        this.direccionService = direccionService;
        this.direccionMapper = direccionMapper;
    }

    @GetMapping("/{id}")
    public DireccionDTO obtenerPorId(@PathVariable Long id) {
        Direccion d = direccionService.obtenerPorId(id);
        return direccionMapper.toDTO(d);
    }
}


