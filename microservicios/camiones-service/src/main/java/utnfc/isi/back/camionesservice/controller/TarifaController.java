package utnfc.isi.back.camionesservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.camionesservice.dto.TarifaDTO;
import utnfc.isi.back.camionesservice.service.TarifaService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/tarifas")
@RequiredArgsConstructor
public class TarifaController {

    private final TarifaService tarifaService;

    @PreAuthorize("hasAnyRole('OPERADOR', 'CLIENTE')")
    @GetMapping("/buscar")
    public TarifaDTO obtenerTarifa(
            @RequestParam Long idTipoCamion,
            @RequestParam BigDecimal pesoContenedor,
            @RequestParam BigDecimal volumenContenedor
    ) {
        return tarifaService.obtenerTarifaPara(idTipoCamion, pesoContenedor, volumenContenedor);
    }

    @PreAuthorize("hasAnyRole('OPERADOR', 'CLIENTE')")
    @GetMapping("/buscar-por-rango")
    public List<TarifaDTO> obtenerTarifasPorRango(
            @RequestParam BigDecimal pesoContenedor,
            @RequestParam BigDecimal volumenContenedor
    ) {
        return tarifaService.obtenerTarifasPorRango(pesoContenedor, volumenContenedor);
    }
}
