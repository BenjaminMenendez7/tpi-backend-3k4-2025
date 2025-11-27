package utnfc.isi.back.contenedoresservice.service.external;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import utnfc.isi.back.contenedoresservice.dto.external.TarifaDTO;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TarifaExternalService {

    private final RestTemplate restTemplate;

    private static final String BASE_URL = "http://camiones-service:8082/api/tarifas/";

    public TarifaDTO obtenerTarifa(Long idTipoCamion, BigDecimal peso, BigDecimal volumen) {

        String url = BASE_URL
                + "?idTipoCamion=" + idTipoCamion
                + "&pesoContenedor=" + peso
                + "&volumenContenedor=" + volumen;

        System.out.println("TARIFA REQUEST => idTipoCamion=" + idTipoCamion
                + " | peso=" + peso
                + " | volumen=" + volumen);


        try {
            return restTemplate.getForObject(url, TarifaDTO.class);

        } catch (RestClientException ex) {
            System.out.println("ERROR TARIFA -> URL=" + url);
            System.out.println("MS CAMIONES NO RESPONDIÓ O NO HAY TARIFA MATCH");

            return new TarifaDTO(
                    new BigDecimal("3000.00"),
                    new BigDecimal("0.50"),
                    new BigDecimal("7000.00")
            );
        }
    }

    public TarifaDTO obtenerTarifaPorRango(BigDecimal peso, BigDecimal volumen) {

        String url = "http://localhost:8082/api/tarifas/buscar-por-rango"
                + "?pesoContenedor=" + peso
                + "&volumenContenedor=" + volumen;

        System.out.println("TARIFA RANGO REQUEST => peso=" + peso + " | volumen=" + volumen);

        try {
            TarifaDTO[] result = restTemplate.getForObject(url, TarifaDTO[].class);

            if (result == null || result.length == 0) {
                System.out.println("NO HAY TARIFAS MATCH POR RANGO, APLICANDO DEFAULT");
                return new TarifaDTO(
                        new BigDecimal("3000.00"),
                        new BigDecimal("0.50"),
                        new BigDecimal("7000.00")
                );
            }

            // Si hay más de una, sacamos promedio
            BigDecimal avgKm = BigDecimal.ZERO;
            BigDecimal avgComb = BigDecimal.ZERO;
            BigDecimal avgEst = BigDecimal.ZERO;

            for (TarifaDTO t : result) {
                avgKm = avgKm.add(t.getCostoKm());
                avgComb = avgComb.add(t.getCostoCombustible());
                avgEst = avgEst.add(t.getCostoEstadiaDeposito());
            }

            int count = result.length;

            return new TarifaDTO(
                    avgKm.divide(BigDecimal.valueOf(count)),
                    avgComb.divide(BigDecimal.valueOf(count)),
                    avgEst.divide(BigDecimal.valueOf(count))
            );

        } catch (RestClientException ex) {
            System.out.println("ERROR TARIFA RANGO -> URL=" + url);
            return new TarifaDTO(
                    new BigDecimal("3000.00"),
                    new BigDecimal("0.50"),
                    new BigDecimal("7000.00")
            );
        }
    }

}
