package utnfc.isi.back.clientesservice.mapper;

import utnfc.isi.back.clientesservice.dto.ClienteDTO;
import utnfc.isi.back.clientesservice.entity.Cliente;

public class ClienteMapper {

    public static ClienteDTO toDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setDocumento(cliente.getDocumento());
        dto.setNombre(cliente.getNombre());
        dto.setApellido(cliente.getApellido());
        dto.setEmail(cliente.getEmail());
        dto.setTelefono(cliente.getTelefono());

        return dto;
    }

}
