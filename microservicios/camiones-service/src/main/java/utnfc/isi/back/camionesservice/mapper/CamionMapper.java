package utnfc.isi.back.camionesservice.mapper;

import utnfc.isi.back.camionesservice.dto.*;
import utnfc.isi.back.camionesservice.entity.Camion;
import utnfc.isi.back.camionesservice.entity.Transportista;

public class CamionMapper {

    public static CamionDTO toDTO(Camion c) {
        CamionDTO dto = new CamionDTO();

        dto.setId(c.getId());
        dto.setPatente(c.getPatente());
        dto.setCapacidadPeso(c.getCapacidadPeso());
        dto.setCapacidadVolumen(c.getCapacidadVolumen());
        dto.setDisponible(c.isDisponible());
        dto.setCostoBaseKm(c.getCostoBaseKm());
        dto.setConsumoCombustiblePromedio(c.getConsumoCombustiblePromedio());

        if (c.getTransportista() != null) {
            dto.setIdTransportista(c.getTransportista().getId());
        }

        if (c.getTipoCamion() != null) {
            dto.setIdTipoCamion(c.getTipoCamion().getId());
        }

        return dto;
    }

    public static CamionConTransportistaDTO toConTransportistaDTO(Camion c) {
        CamionConTransportistaDTO dto = new CamionConTransportistaDTO();

        dto.setId(c.getId());
        dto.setPatente(c.getPatente());
        dto.setCapacidadPeso(c.getCapacidadPeso());
        dto.setCapacidadVolumen(c.getCapacidadVolumen());
        dto.setDisponible(c.isDisponible());
        dto.setCostoBaseKm(c.getCostoBaseKm());
        dto.setConsumoCombustiblePromedio(c.getConsumoCombustiblePromedio());

        Transportista t = c.getTransportista();
        if (t != null) {
            dto.setTransportistaId(t.getId());
            dto.setDocumento(t.getDocumento());
            dto.setNombre(t.getNombre());
            dto.setApellido(t.getApellido());
            dto.setEmail(t.getEmail());
            dto.setTelefono(t.getTelefono());
        }

        return dto;
    }
}
