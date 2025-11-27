package utnfc.isi.back.contenedoresservice.service;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class OsrmService {

    private static final String OSRM_BASE_URL = "http://osrm:5000/route/v1/driving/";

    private final RestTemplate restTemplate;

    public OsrmService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    /**
     * Obtiene la distancia REAL (en km) entre dos coordenadas usando OSRM.
     * Si falla, retorna fallback aproximado.
     */
    public BigDecimal obtenerDistanciaKm(BigDecimal lat1, BigDecimal lon1,
                                         BigDecimal lat2, BigDecimal lon2) {

        try {
            String url = OSRM_BASE_URL
                    + lon1.toPlainString() + "," + lat1.toPlainString()
                    + ";" + lon2.toPlainString() + "," + lat2.toPlainString()
                    + "?overview=false";

            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            Double distanciaMetros = root
                    .get("routes")
                    .get(0)
                    .get("distance")
                    .asDouble();

            BigDecimal distanciaKm = BigDecimal
                    .valueOf(distanciaMetros)
                    .divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);

            if (distanciaKm.compareTo(BigDecimal.ZERO) <= 0) {
                return fallback(lat1, lon1, lat2, lon2);
            }

            return distanciaKm;

        } catch (Exception e) {
            System.err.println("⚠ OSRM no disponible. Usando fallback. Motivo: " + e.getMessage());
            return fallback(lat1, lon1, lat2, lon2);
        }
    }

    /**
     * Valor de emergencia si OSRM no responde.
     * Podés ajustar si querés algo más inteligente.
     */
    private BigDecimal fallback(BigDecimal lat1, BigDecimal lon1,
                                BigDecimal lat2, BigDecimal lon2) {
        return BigDecimal.TEN; // 10 km
    }

}
