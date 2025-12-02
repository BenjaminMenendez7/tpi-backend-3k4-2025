package utnfc.isi.back.camionesservice.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.camionesservice.dto.BuscarTarifaRequestDTO;
import utnfc.isi.back.camionesservice.dto.CrearTarifaRequestDTO;
import utnfc.isi.back.camionesservice.dto.TarifaDTO;
import utnfc.isi.back.camionesservice.dto.TarifaRequestDTO;
import utnfc.isi.back.camionesservice.repository.TarifaRepository;
import utnfc.isi.back.camionesservice.service.TarifaService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/tarifas")
@AllArgsConstructor
public class TarifaController {
    private final TarifaService tarifaService;

    @PreAuthorize("hasAnyRole('OPERADOR', 'CLIENTE')")
    @PostMapping("/buscar")
    public ResponseEntity<TarifaDTO> obtenerTarifa(@Valid @RequestBody BuscarTarifaRequestDTO request) {
        TarifaDTO tarifa = tarifaService.obtenerTarifaPara(
                request.getIdTipoCamion(),
                request.getPesoContenedor(),
                request.getVolumenContenedor()
        );
        return ResponseEntity.ok(tarifa);
    }

    @PreAuthorize("hasAnyRole('OPERADOR', 'CLIENTE')")
    @GetMapping("/buscar-por-rango")
    public ResponseEntity<List<TarifaDTO>> obtenerTarifasPorRango(
            @RequestParam BigDecimal pesoContenedor,
            @RequestParam BigDecimal volumenContenedor
    ) {
        List<TarifaDTO> tarifas = tarifaService.obtenerTarifasPorRango(pesoContenedor, volumenContenedor);
        return ResponseEntity.ok(tarifas);
    }

    @PreAuthorize("hasRole('OPERADOR')")
    @PostMapping("/crear")
    public ResponseEntity<TarifaDTO> crearTarifa(@Valid @RequestBody CrearTarifaRequestDTO request) {
        TarifaDTO nuevaTarifa = tarifaService.crearTarifa(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaTarifa);
    }
}
