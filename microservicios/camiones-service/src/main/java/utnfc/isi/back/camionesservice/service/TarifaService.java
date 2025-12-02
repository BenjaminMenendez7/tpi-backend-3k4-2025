package utnfc.isi.back.camionesservice.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utnfc.isi.back.camionesservice.dto.CrearTarifaRequestDTO;
import utnfc.isi.back.camionesservice.dto.TarifaDTO;
import utnfc.isi.back.camionesservice.dto.TarifaRequestDTO;
import utnfc.isi.back.camionesservice.entity.Tarifa;
import utnfc.isi.back.camionesservice.entity.TipoCamion;
import utnfc.isi.back.camionesservice.mapper.CamionMapper;
import utnfc.isi.back.camionesservice.mapper.TarifaMapper;
import utnfc.isi.back.camionesservice.repository.TarifaRepository;
import utnfc.isi.back.camionesservice.repository.TipoCamionRepository;
import utnfc.isi.back.common.exceptions.ReglaNegocioException;
import utnfc.isi.back.common.exceptions.ResourceNotFoundException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarifaService {

    private static final Logger log = LoggerFactory.getLogger(TarifaService.class);

    private final TarifaRepository tarifaRepository;
    private final TipoCamionRepository tipoCamionRepository;
    private final TarifaMapper tarifaMapper;
    /**
     * Método original: obtiene la tarifa para un tipo de camión específico
     * (ej. Regla 4 o selección explícita).
     */
    public TarifaDTO obtenerTarifaPara(Long idTipoCamion,
                                       BigDecimal pesoContenedor,
                                       BigDecimal volumenContenedor) {

        log.debug("Buscando tarifa -> idTipoCamion={} | peso={} | volumen={}",
                idTipoCamion, pesoContenedor, volumenContenedor);

        List<Tarifa> tarifas = tarifaRepository.findByTipoCamionId(idTipoCamion);

        return tarifas.stream()
                .filter(t -> enRango(t.getRangoPeso(), pesoContenedor))
                .filter(t -> enRango(t.getRangoVolumen(), volumenContenedor))
                .findFirst()
                .map(t -> new TarifaDTO(
                        t.getCostoKm(),
                        t.getCostoCombustible(),
                        t.getCostoEstadiaDeposito()
                ))
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "No existe tarifa aplicable para los valores recibidos"));
    }

    /**
     * Nuevo método: obtiene todas las tarifas aplicables por rango
     * (ej. Regla 3).
     */
    public List<TarifaDTO> obtenerTarifasPorRango(BigDecimal peso, BigDecimal volumen) {
        return tarifaRepository.findAll()
                .stream()
                .filter(t -> enRango(t.getRangoPeso(), peso))
                .filter(t -> enRango(t.getRangoVolumen(), volumen))
                .map(t -> new TarifaDTO(
                        t.getCostoKm(),
                        t.getCostoCombustible(),
                        t.getCostoEstadiaDeposito()
                ))
                .toList();
    }

    /**
     * Calcula una tarifa promedio entre todas las tarifas aplicables.
     * Útil para estimaciones iniciales cuando hay varios camiones elegibles.
     */
    public TarifaDTO calcularTarifaPromedio(BigDecimal peso, BigDecimal volumen) {
        List<TarifaDTO> tarifas = obtenerTarifasPorRango(peso, volumen);

        if (tarifas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No existen tarifas aplicables para los valores recibidos");
        }

        BigDecimal promedioKm = tarifas.stream()
                .map(TarifaDTO::getCostoKm)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(tarifas.size()), BigDecimal.ROUND_HALF_UP);

        BigDecimal promedioCombustible = tarifas.stream()
                .map(TarifaDTO::getCostoCombustible)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(tarifas.size()), BigDecimal.ROUND_HALF_UP);

        BigDecimal promedioEstadia = tarifas.stream()
                .map(TarifaDTO::getCostoEstadiaDeposito)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(tarifas.size()), BigDecimal.ROUND_HALF_UP);

        return new TarifaDTO(promedioKm, promedioCombustible, promedioEstadia);
    }

    /**
     * Helper para validar rangos tipo "8000-15000" o "0-999999".
     */
    private boolean enRango(String rango, BigDecimal valor) {
        if (rango == null || rango.isBlank()) return false;

        String[] partes = rango.split("-");
        if (partes.length != 2) return false;

        try {
            BigDecimal min = new BigDecimal(partes[0].trim());
            BigDecimal max = new BigDecimal(partes[1].trim());
            return valor.compareTo(min) >= 0 && valor.compareTo(max) <= 0;
        } catch (NumberFormatException e) {
            log.warn("Formato de rango inválido: {}", rango);
            return false;
        }
    }

    public TarifaDTO crearTarifa(CrearTarifaRequestDTO request) {
        boolean existe = tarifaRepository.existsByTipoCamionIdAndRangoPesoAndRangoVolumen(
                request.getIdTipoCamion(),
                request.getRangoPeso(),
                request.getRangoVolumen()
        );

        if (existe) {
            throw new ReglaNegocioException("TARIFA_DUPLICADA",
                    "Ya existe una tarifa para esa combinación de tipoCamion, rangoPeso y rangoVolumen");
        }

        TipoCamion tipoCamion = tipoCamionRepository.findById(request.getIdTipoCamion())
                .orElseThrow(() -> new ResourceNotFoundException("TipoCamion no encontrado"));

        Tarifa tarifa = tarifaMapper.toEntity(request);
        tarifa.setTipoCamion(tipoCamion);

        Tarifa guardada = tarifaRepository.save(tarifa);
        return tarifaMapper.toDTO(guardada);
    }
}
