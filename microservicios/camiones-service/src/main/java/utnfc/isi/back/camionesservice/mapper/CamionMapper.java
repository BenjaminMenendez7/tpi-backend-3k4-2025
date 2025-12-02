package utnfc.isi.back.camionesservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import utnfc.isi.back.camionesservice.dto.*;
import utnfc.isi.back.camionesservice.entity.Camion;

@Mapper(componentModel = "spring")
public interface CamionMapper {

    @Mapping(target = "idTransportista", source = "transportista.id")
    @Mapping(target = "idTipoCamion", source = "tipoCamion.id")
    CamionDTO toDTO(Camion camion);

    @Mapping(target = "transportista.id", source = "idTransportista")
    @Mapping(target = "tipoCamion.id", source = "idTipoCamion")
    Camion toEntity(CamionRequestDTO dto);

    @Mapping(target = "transportista.id", source = "idTransportista")
    @Mapping(target = "tipoCamion.id", source = "idTipoCamion")
    void updateEntityFromDto(CamionRequestDTO dto, @MappingTarget Camion camion);

    @Mapping(target = "transportistaId", source = "transportista.id")
    @Mapping(target = "documento", source = "transportista.documento")
    @Mapping(target = "nombre", source = "transportista.nombre")
    @Mapping(target = "apellido", source = "transportista.apellido")
    @Mapping(target = "email", source = "transportista.email")
    @Mapping(target = "telefono", source = "transportista.telefono")
    CamionConTransportistaDTO toConTransportistaDTO(Camion camion);
}
