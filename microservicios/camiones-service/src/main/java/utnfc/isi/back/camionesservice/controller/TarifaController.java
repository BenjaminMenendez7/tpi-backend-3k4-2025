package utnfc.isi.back.camionesservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.camionesservice.dto.TarifaDTO;
import utnfc.isi.back.camionesservice.service.TarifaService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/tarifas")
@RequiredArgsConstructor
public class TarifaController {

    private final TarifaService tarifaService;

    @GetMapping("/buscar")
    public TarifaDTO obtenerTarifa(
            @RequestParam Long idTipoCamion,
            @RequestParam BigDecimal pesoContenedor,
            @RequestParam BigDecimal volumenContenedor
    ) {
        return tarifaService.obtenerTarifaPara(idTipoCamion, pesoContenedor, volumenContenedor);
    }

    @GetMapping("/buscar-por-rango")
    public List<TarifaDTO> obtenerTarifasPorRango(
            @RequestParam BigDecimal pesoContenedor,
            @RequestParam BigDecimal volumenContenedor
    ) {
        return tarifaService.obtenerTarifasPorRango(pesoContenedor, volumenContenedor);
    }
}
