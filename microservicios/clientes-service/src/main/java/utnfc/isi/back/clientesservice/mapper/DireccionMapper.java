package utnfc.isi.back.clientesservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utnfc.isi.back.clientesservice.dto.DireccionDTO;
import utnfc.isi.back.clientesservice.entity.Direccion;

@Mapper(componentModel = "spring")
public interface DireccionMapper {

    @Mapping(target = "calle.id", source = "calleId")
    @Mapping(target = "cliente.id", source = "clienteId")
    @Mapping(target = "calle.nombre", source = "nombreCalle")
    @Mapping(target = "calle.barrio.nombre", source = "barrio")
    @Mapping(target = "calle.barrio.ciudad.nombre", source = "ciudad")
    @Mapping(target = "calle.barrio.ciudad.provincia.nombre", source = "provincia")
    @Mapping(target = "calle.barrio.ciudad.provincia.pais.nombre", source = "pais")
    Direccion toEntity(DireccionDTO dto);

    @Mapping(target = "calleId", source = "calle.id")
    @Mapping(target = "nombreCalle", source = "calle.nombre")
    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "barrio", source = "calle.barrio.nombre")
    @Mapping(target = "ciudad", source = "calle.barrio.ciudad.nombre")
    @Mapping(target = "provincia", source = "calle.barrio.ciudad.provincia.nombre")
    @Mapping(target = "pais", source = "calle.barrio.ciudad.provincia.pais.nombre")
    DireccionDTO toDTO(Direccion direccion);
}
