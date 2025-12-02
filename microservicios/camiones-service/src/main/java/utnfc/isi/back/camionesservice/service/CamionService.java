package utnfc.isi.back.camionesservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import utnfc.isi.back.camionesservice.dto.*;
import utnfc.isi.back.camionesservice.entity.*;
import utnfc.isi.back.camionesservice.mapper.CamionMapper;
import utnfc.isi.back.camionesservice.repository.*;
import utnfc.isi.back.common.exceptions.ResourceNotFoundException;
import utnfc.isi.back.common.exceptions.ReglaNegocioException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CamionService {

    private final CamionRepository camionRepository;
    private final TransportistaRepository transportistaRepository;
    private final CamionMapper camionMapper;
    private final TarifaService tarifaService;


    // ----------- CRUD básico -----------

    public List<CamionDTO> obtenerTodos() {
        return camionRepository.findAll()
                .stream()
                .map(camionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CamionDTO obtenerPorId(Long id) {
        Camion camion = camionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Camión no encontrado con id: " + id));
        return camionMapper.toDTO(camion);
    }

    public CamionDTO crearCamion(CamionRequestDTO dto) {
        validarPatenteUnica(dto.getPatente());

        Camion camion = camionMapper.toEntity(dto);

        camion.setDisponible(true);
        camion.setConsumoCombustiblePromedio(BigDecimal.ZERO);

        if (dto.getIdTipoCamion() != null) {
            TarifaDTO tarifa = tarifaService.obtenerTarifaPara(
                    dto.getIdTipoCamion(),
                    dto.getCapacidadPeso(),
                    dto.getCapacidadVolumen()
            );
            camion.setCostoBaseKm(tarifa.getCostoKm());
        }

        manejarTransportista(camion, dto.getIdTransportista());

        Camion guardado = camionRepository.save(camion);
        return camionMapper.toDTO(guardado);
    }


    public CamionDTO actualizarCamion(Long id, CamionRequestDTO dto) {
        Camion camion = camionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Camión no encontrado"));

        if (dto.getPatente() != null && !dto.getPatente().equals(camion.getPatente())) {
            validarPatenteUnica(dto.getPatente());
        }

        camionMapper.updateEntityFromDto(dto, camion);
        manejarTransportista(camion, dto.getIdTransportista());

        // Recalcular costoBaseKm si cambió tipo, peso o volumen
        if (dto.getIdTipoCamion() != null || dto.getCapacidadPeso() != null || dto.getCapacidadVolumen() != null) {
            TarifaDTO tarifa = tarifaService.obtenerTarifaPara(
                    dto.getIdTipoCamion() != null ? dto.getIdTipoCamion() : camion.getTipoCamion().getId(),
                    dto.getCapacidadPeso() != null ? dto.getCapacidadPeso() : camion.getCapacidadPeso(),
                    dto.getCapacidadVolumen() != null ? dto.getCapacidadVolumen() : camion.getCapacidadVolumen()
            );
            camion.setCostoBaseKm(tarifa.getCostoKm());
        }

        Camion actualizado = camionRepository.save(camion);
        return camionMapper.toDTO(actualizado);
    }

    public void eliminarCamion(Long id) {
        if (!camionRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe camión con id: " + id);
        }
        camionRepository.deleteById(id);
    }

    // ----------- Consultas específicas -----------

    public List<CamionDTO> obtenerDisponibles() {
        return camionRepository.findByDisponibleTrue()
                .stream()
                .map(camionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<CamionDTO> obtenerPorTransportista(Long idTransportista) {
        return camionRepository.findByTransportistaId(idTransportista)
                .stream()
                .map(camionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CamionDTO obtenerPorPatente(String patente) {
        Camion camion = camionRepository.findByPatente(patente)
                .orElseThrow(() -> new ResourceNotFoundException("Camión no encontrado con patente: " + patente));
        return camionMapper.toDTO(camion);
    }


    public CamionConTransportistaDTO obtenerDetalleConTransportista(Long idCamion) {
        Camion camion = camionRepository.findById(idCamion)
                .orElseThrow(() -> new ResourceNotFoundException("Camión no encontrado con id: " + idCamion));
        return camionMapper.toConTransportistaDTO(camion);
    }

    // ----------- Helpers internos -----------

    private void validarPatenteUnica(String patente) {
        Optional<Camion> existente = camionRepository.findByPatente(patente);
        if (existente.isPresent()) {
            throw new ReglaNegocioException("Ya existe un camión con la patente: " + patente, "PATENTE_DUPLICADA");
        }
    }

    private void manejarTransportista(Camion camion, Long idTransportista) {
        if (idTransportista == null) {
            camion.setTransportista(null);
            return;
        }

        Transportista transportista = transportistaRepository.findById(idTransportista)
                .orElseThrow(() -> new ResourceNotFoundException("Transportista no encontrado con id: " + idTransportista));
        camion.setTransportista(transportista);
    }

    public CamionDTO cambiarDisponibilidad(Long idCamion, boolean disponible) {
        Camion camion = camionRepository.findById(idCamion)
                .orElseThrow(() -> new ResourceNotFoundException("Camión no encontrado con id: " + idCamion));

        camion.setDisponible(disponible);
        Camion actualizado = camionRepository.save(camion);

        return camionMapper.toDTO(actualizado);
    }
}
