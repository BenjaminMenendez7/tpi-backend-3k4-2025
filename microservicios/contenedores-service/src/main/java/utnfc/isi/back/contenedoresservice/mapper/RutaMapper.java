package utnfc.isi.back.contenedoresservice.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import utnfc.isi.back.contenedoresservice.client.DireccionClient;
import utnfc.isi.back.contenedoresservice.entity.Ruta;
import utnfc.isi.back.contenedoresservice.dto.RutaDTO;
import utnfc.isi.back.contenedoresservice.entity.Tramo;

@Component
@RequiredArgsConstructor
public class RutaMapper {

    // ⚠️ Lo dejamos, pero NO lo usamos para evitar romper el TP
    private final DireccionClient direccionClient;

    public RutaDTO toDTO(Ruta entity) {
        if (entity == null) return null;

        RutaDTO dto = new RutaDTO();
        dto.setId(entity.getId());
        dto.setDistanciaTotalKm(entity.getDistanciaTotalKm());
        dto.setTiempoTotalMinutos(entity.getTiempoTotalMinutos());
        dto.setCostoTotal(entity.getCostoTotal());
        dto.setSeleccionada(entity.isSeleccionada());
        dto.setIdSolicitud(entity.getSolicitud() != null ? entity.getSolicitud().getId() : null);

        // =============================
        // ORIGEN Y DESTINO (FALLBACK)
        // =============================
        if (entity.getTramos() != null && !entity.getTramos().isEmpty()) {

            Tramo primer = entity.getTramos().get(0);
            Tramo ultimo = entity.getTramos().get(entity.getTramos().size() - 1);

            // Usamos fallback temporal válido
            dto.setOrigenNombre("Origen " + primer.getOrigen());
            dto.setDestinoNombre("Destino " + ultimo.getDestino());
        }

        return dto;
    }

    public Ruta toEntity(RutaDTO dto) {
        if (dto == null) return null;

        Ruta entity = new Ruta();
        entity.setId(dto.getId());
        entity.setDistanciaTotalKm(dto.getDistanciaTotalKm());
        entity.setTiempoTotalMinutos(dto.getTiempoTotalMinutos());
        entity.setCostoTotal(dto.getCostoTotal());
        entity.setSeleccionada(dto.isSeleccionada());

        entity.setSolicitud(null);
        entity.setTramos(null);

        return entity;
    }
}
