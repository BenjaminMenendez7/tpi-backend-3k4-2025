package utnfc.isi.back.contenedoresservice.mapper;

import org.springframework.stereotype.Component;
import utnfc.isi.back.contenedoresservice.entity.Deposito;
import utnfc.isi.back.contenedoresservice.dto.DepositoDTO;

@Component
public class DepositoMapper {

    public DepositoDTO toDTO(Deposito entity) {
        if (entity == null) return null;

        DepositoDTO dto = new DepositoDTO();
        dto.setIdDireccion(entity.getIdDireccion());
        dto.setNombre(entity.getNombre());
        dto.setCostoEstadiaDiario(entity.getCostoEstadiaDiario());

        return dto;
    }

    public Deposito toEntity(DepositoDTO dto) {
        if (dto == null) return null;

        Deposito entity = new Deposito();
        entity.setIdDireccion(dto.getIdDireccion());
        entity.setNombre(dto.getNombre());
        entity.setCostoEstadiaDiario(dto.getCostoEstadiaDiario());

        return entity;
    }
}
