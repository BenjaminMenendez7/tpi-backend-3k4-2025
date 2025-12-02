package utnfc.isi.back.camionesservice.mapper;

import org.mapstruct.Mapper;
import utnfc.isi.back.camionesservice.dto.CrearTarifaRequestDTO;
import utnfc.isi.back.camionesservice.dto.TarifaDTO;
import utnfc.isi.back.camionesservice.dto.TarifaRequestDTO;
import utnfc.isi.back.camionesservice.entity.Tarifa;

@Mapper(componentModel = "spring")
public interface TarifaMapper {

    // Conversión para los casos donde ya usás TarifaRequestDTO
    Tarifa toEntity(TarifaRequestDTO dto);

    // Conversión para crear tarifas con CrearTarifaRequestDTO
    Tarifa toEntity(CrearTarifaRequestDTO dto);

    // Conversión de entidad a DTO de respuesta
    TarifaDTO toDTO(Tarifa entity);
}
