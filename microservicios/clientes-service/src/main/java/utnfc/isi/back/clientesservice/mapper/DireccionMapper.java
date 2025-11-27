package utnfc.isi.back.clientesservice.mapper;

import org.springframework.stereotype.Component;
import utnfc.isi.back.clientesservice.dto.DireccionDTO;
import utnfc.isi.back.clientesservice.entity.Direccion;

@Component
public class DireccionMapper {

    public static DireccionDTO toDTO(Direccion d) {
        DireccionDTO dto = new DireccionDTO();

        dto.setId(d.getId());
        dto.setNroCalle(d.getNroCalle());
        dto.setPiso(d.getPiso());
        dto.setDpto(d.getDpto());
        dto.setObservaciones(d.getObservaciones());
        dto.setCodigoPostal(d.getCodigoPostal());

        // Relaciones ManyToOne → solo enviamos IDs
        dto.setCalleId(d.getCalle().getId());
        dto.setNombreCalle(d.getCalle().getNombre());

        dto.setClienteId(d.getCliente().getId());

        return dto;
    }
}
