package utnfc.isi.back.camionesservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utnfc.isi.back.camionesservice.dto.TarifaDTO;
import utnfc.isi.back.camionesservice.entity.Tarifa;
import utnfc.isi.back.camionesservice.repository.TarifaRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarifaService {

    private final TarifaRepository tarifaRepository;


    /**
     * Metodo original, utilizado cuando ya hay tipo de camión definido
     * (por ejemplo en Regla 4 o por selección explícita)
     */
    public TarifaDTO obtenerTarifaPara(Long idTipoCamion,
                                       BigDecimal pesoContenedor,
                                       BigDecimal volumenContenedor) {

        System.out.println("DEBUG TARIFA -> idTipoCamion=" + idTipoCamion
                + " | peso=" + pesoContenedor
                + " | volumen=" + volumenContenedor);

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
     * Nuevo metodo para búsqueda usando solo rangos — válido para Regla 3
     */
    public List<TarifaDTO> obtenerTarifasPorRango(BigDecimal peso, BigDecimal volumen) {

        return tarifaRepository.findAll()
                .stream()
                .filter(t -> enRango(t.getRangoPeso(), peso))
                .filter(t -> enRango(t.getRangoVolumen(), volumen))
                .map(t -> {
                    TarifaDTO dto = new TarifaDTO();
                    dto.setCostoKm(t.getCostoKm());
                    dto.setCostoCombustible(t.getCostoCombustible());
                    dto.setCostoEstadiaDeposito(t.getCostoEstadiaDeposito());
                    return dto;
                })
                .toList();
    }


    /**
     * Helper para validar rangos tipo "8000-15000" o "0-999999"
     */
    private boolean enRango(String rango, BigDecimal valor) {
        if (rango == null || rango.isBlank()) return false;

        String[] partes = rango.split("-");
        if (partes.length != 2) return false;

        BigDecimal min = new BigDecimal(partes[0].trim());
        BigDecimal max = new BigDecimal(partes[1].trim());

        return valor.compareTo(min) >= 0 && valor.compareTo(max) <= 0;
    }

}
