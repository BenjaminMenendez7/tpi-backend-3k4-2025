package utnfc.isi.back.contenedoresservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import utnfc.isi.back.contenedoresservice.dto.external.TarifaDTO;
import utnfc.isi.back.contenedoresservice.entity.Contenedor;
import utnfc.isi.back.contenedoresservice.dto.CamionDTO;
import utnfc.isi.back.contenedoresservice.exception.ReglaNegocioException;
import utnfc.isi.back.contenedoresservice.service.external.TarifaExternalService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CamionAsignacionService {

    private final TarifaExternalService tarifaExternalService;

    public CamionDTO seleccionarCamion(Contenedor contenedor, List<CamionDTO> camionesDisponibles) {

        log.info("[Regla 1] Validando capacidad para contenedor ID={}, peso={}, volumen={}",
                contenedor.getId(), contenedor.getPeso(), contenedor.getVolumen());

        if (camionesDisponibles == null || camionesDisponibles.isEmpty()) {
            log.warn("[Regla 1] No hay camiones disponibles.");
            throw new ReglaNegocioException(
                    "RN001",
                    "Regla 1 – Capacidad del camión: no hay camiones disponibles."
            );
        }

        log.info("[Regla 1] Camiones recibidos: {}", camionesDisponibles.size());

        List<CamionDTO> aptos = new ArrayList<>();

        for (CamionDTO c : camionesDisponibles) {
            boolean pesoOk = c.getCapacidadPeso().compareTo(contenedor.getPeso()) >= 0;
            boolean volOk = c.getCapacidadVolumen().compareTo(contenedor.getVolumen()) >= 0;

            if (pesoOk && volOk) {
                aptos.add(c);
            }
        }

        if (aptos.isEmpty()) {
            log.warn("[Regla 1] No hay camiones aptos para contenedor ID={}", contenedor.getId());
            throw new ReglaNegocioException(
                    "RN001",
                    "Regla 1 – Capacidad del camión: el contenedor excede la capacidad del camión."
            );
        }

        // *** REGLA 2 - CALCULO DE TARIFA PROMEDIO ***
        TarifaDTO tarifaPromedio = obtenerTarifaPromedio(
                aptos,
                contenedor.getPeso(),
                contenedor.getVolumen()
        );

        log.info("[Regla 2] Tarifa promedio para contenedor ID {} => km={}, comb={}, estadia={}",
                contenedor.getId(),
                tarifaPromedio.getCostoKm(),
                tarifaPromedio.getCostoCombustible(),
                tarifaPromedio.getCostoEstadiaDeposito()
        );

        // *** REGLA 1 - Seleccion del camion ***
        CamionDTO seleccionado = aptos.get(0);
        log.info("[Regla 1] Camión asignado: ID={}, patente={}",
                seleccionado.getId(),
                seleccionado.getPatente()
        );

        return seleccionado;
    }

    public TarifaDTO obtenerTarifaPromedio(List<CamionDTO> aptos, BigDecimal peso, BigDecimal volumen) {

        List<TarifaDTO> tarifas = new ArrayList<>();

        for (CamionDTO c : aptos) {
            TarifaDTO t = tarifaExternalService.obtenerTarifa(
                    c.getIdTipoCamion(),
                    peso,
                    volumen
            );
            if (t != null) tarifas.add(t);
        }

        if (tarifas.isEmpty()) {
            throw new ReglaNegocioException(
                    "RN002",
                    "Regla 2 – No se pudo obtener tarifa de ningún camión disponible."
            );
        }

        // PROMEDIO ARITMETICO
        BigDecimal totalKm = BigDecimal.ZERO;
        BigDecimal totalComb = BigDecimal.ZERO;
        BigDecimal totalEstadia = BigDecimal.ZERO;

        for (TarifaDTO t : tarifas) {
            totalKm = totalKm.add(t.getCostoKm());
            totalComb = totalComb.add(t.getCostoCombustible());
            totalEstadia = totalEstadia.add(t.getCostoEstadiaDeposito());
        }

        BigDecimal divisor = BigDecimal.valueOf(tarifas.size());

        return new TarifaDTO(
                totalKm.divide(divisor, 2, RoundingMode.HALF_UP),
                totalComb.divide(divisor, 2, RoundingMode.HALF_UP),
                totalEstadia.divide(divisor, 2, RoundingMode.HALF_UP)
        );
    }
}
