package utnfc.isi.back.camionesservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuscarTarifaRequestDTO {

    @NotNull(message = "El idTipoCamion es obligatorio")
    private Long idTipoCamion;

    @NotNull(message = "El peso del contenedor es obligatorio")
    private BigDecimal pesoContenedor;

    @NotNull(message = "El volumen del contenedor es obligatorio")
    private BigDecimal volumenContenedor;
}