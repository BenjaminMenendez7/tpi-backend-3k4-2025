package utnfc.isi.back.clientesservice.mapper;

import org.mapstruct.Mapper;
import utnfc.isi.back.clientesservice.dto.ClienteConDireccionesDTO;
import utnfc.isi.back.clientesservice.dto.ClienteDTO;
import utnfc.isi.back.clientesservice.entity.Cliente;

@Mapper(componentModel = "spring", uses = {DireccionMapper.class})
public interface ClienteMapper {

    ClienteDTO toDTO(Cliente cliente);

    Cliente toEntity(ClienteDTO dto);

    ClienteConDireccionesDTO toConDireccionesDTO(Cliente cliente);

    Cliente toEntityConDirecciones(ClienteConDireccionesDTO dto);
}
