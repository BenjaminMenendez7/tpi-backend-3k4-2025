package utnfc.isi.back.contenedoresservice.service;

import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import utnfc.isi.back.contenedoresservice.client.CamionClient;
import utnfc.isi.back.contenedoresservice.client.GeolocalizacionClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utnfc.isi.back.contenedoresservice.dto.*;
import utnfc.isi.back.contenedoresservice.entity.*;
import utnfc.isi.back.contenedoresservice.exception.GlobalExceptionHandler;
import utnfc.isi.back.contenedoresservice.exception.ReglaNegocioException;
import utnfc.isi.back.contenedoresservice.exception.ResourceNotFoundException;
import utnfc.isi.back.contenedoresservice.exception.TransicionInvalidaException;
import utnfc.isi.back.contenedoresservice.mapper.*;
import utnfc.isi.back.contenedoresservice.repository.*;
import utnfc.isi.back.contenedoresservice.service.enums.EstadoSolicitudEnum;
import utnfc.isi.back.contenedoresservice.service.enums.EstadoTramoEnum;
import utnfc.isi.back.contenedoresservice.service.external.CamionExternalService;
import utnfc.isi.back.contenedoresservice.service.external.GeolocalizacionExternalService;
import utnfc.isi.back.contenedoresservice.service.external.TarifaExternalService;
import utnfc.isi.back.contenedoresservice.dto.external.TarifaDTO;  // ✔ el correcto


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
@Getter @Setter
public class SolicitudService {

    private final SolicitudMapper solicitudMapper;
    private Long estadiaRealMinutos;
    private final CamionClient camionClient;
    private final EstadoSolicitudRepository estadoSolicitudRepository;
    private final SolicitudEstadoHistorialService solicitudEstadoHistorialService;
    private final SolicitudEstadoValidator estadoValidator;
    private final SolicitudEstadoHistorialRepository historialRepository;
    private final SolicitudEstadoHistorialMapper historialMapper;
    private final CamionExternalService camionExternalService;
    private final TarifaExternalService tarifaExternalService;
    private final OsrmService osrmService;
    private final GeolocalizacionClient geoClient;
    private final GeolocalizacionExternalService geolocalizacionExternalService;
    private final RestTemplate restTemplate = new RestTemplate();


    private final SolicitudRepository solicitudRepository;
    private final RutaRepository rutaRepository;
    private final TramoRepository tramoRepository;

    private final ContenedorMapper contenedorMapper;
    private final RutaMapper rutaMapper;
    private final TramoMapper tramoMapper;

    private final CamionAsignacionService camionAsignacionService;
    private static final Logger log = LoggerFactory.getLogger(SolicitudService.class);


    // ===============================
    // Métodos CRUD básicos
    // ===============================
    public List<Solicitud> findAll() {
        return solicitudRepository.findAll();
    }

    public Solicitud findById(Long id) {
        return solicitudRepository.findById(id).orElse(null);
    }

    private Solicitud save(Solicitud solicitud) {
        return solicitudRepository.save(solicitud);
    }

    @Transactional
    public SolicitudDetalleDTO save(SolicitudDTO solicitudDto) {
        Solicitud solicitud = solicitudMapper.toEntity(solicitudDto);

        EstadoSolicitud estadoBorrador = estadoSolicitudRepository.findByNombre(EstadoSolicitudEnum.BORRADOR.name())
                        .orElseThrow(() -> new ResourceNotFoundException("Estado BORRADOR no encontrado", solicitud.getId()));

        solicitud.setEstadoSolicitud(estadoBorrador);
        solicitud.setFechaCreacion(LocalDateTime.now());

        solicitudRepository.save(solicitud);

        return buildDetalle(solicitud.getId());
    }

    @Transactional
    public void delete(Long id) {
        solicitudRepository.deleteById(id);
    }

    // ===============================
    // Construir detalle de solicitud
    // ===============================
    @Transactional(readOnly = true)
    public SolicitudDetalleDTO buildDetalle(Long idSolicitud) {

        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada"));

        SolicitudDetalleDTO detalle = new SolicitudDetalleDTO();
        detalle.setId(solicitud.getId());
        detalle.setIdCliente(solicitud.getIdCliente());
        detalle.setFechaSolicitud(solicitud.getFechaCreacion());
        detalle.setEstadoSolicitud(solicitud.getEstadoSolicitud().getNombre());
        detalle.setContenedor(solicitud.getContenedor() != null ? contenedorMapper.toDTO(solicitud.getContenedor()) : null);

        // ================================
        // RUTAS
        // ================================
        List<Ruta> rutas = rutaRepository.findBySolicitud_Id(idSolicitud);

        if (!rutas.isEmpty()) {

            Ruta rutaSel = rutas.stream()
                    .filter(Ruta::isSeleccionada)
                    .findFirst()
                    .orElse(rutas.get(0));

            var tramosOrdenados = tramoRepository.findByRuta_IdOrderByIdAsc(rutaSel.getId());
            rutaSel.setTramos(tramosOrdenados);

            detalle.setRuta(rutaMapper.toDTO(rutaSel));

            detalle.setTramos(
                    tramosOrdenados.stream()
                            .map(tramo -> {
                                TramoDTO tramoDTO = tramoMapper.toDTO(tramo);
                                // Aseguramos que el nombre del estado del tramo se obtenga correctamente
                                if (tramo.getEstadoTramo() != null) {
                                    tramoDTO.setEstadoTramo(tramo.getEstadoTramo().getNombre());
                                } else {
                                    tramoDTO.setEstadoTramo("Desconocido");
                                }
                                return tramoDTO;
                            })
                            .toList()
            );

        } else {
            detalle.setRuta(null);
            detalle.setTramos(List.of());
        }


        // ================================
        // COSTOS Y TIEMPOS REALES REGISTRADOS
        // ================================
        detalle.setCostoFinal(solicitud.getCostoFinal());
        detalle.setTiempoReal(solicitud.getTiempoReal());

        // lo estimado
        detalle.setCostoTotal(solicitud.getCostoEstimado());

        // ================================
        // HISTORIAL DE ESTADOS
        // ================================
        detalle.setHistorialEstado(
                (HistorialEstadoDTO) historialRepository.findByIdSolicitudOrderByFechaRegistroAsc(idSolicitud)
                        .stream()
                        .map(historialMapper::toDTO)
                        .toList()
        );

        return detalle;
    }

    @Transactional(readOnly = true)
    public List<SolicitudDetalleDTO> findAllDetalle() {
        return solicitudRepository.findAll().stream()
                .map(s -> {
                    SolicitudDetalleDTO dto = new SolicitudDetalleDTO();
                    dto.setId(s.getId());
                    dto.setFechaSolicitud(s.getFechaCreacion());
                    dto.setEstadoSolicitud(s.getEstadoSolicitud().getNombre());
                    dto.setContenedor(s.getContenedor() != null ? contenedorMapper.toDTO(s.getContenedor()) : null);
                    dto.setCostoFinal(s.getCostoFinal());
                    dto.setCostoTotal(s.getCostoEstimado());
                    dto.setTiempoReal(s.getTiempoReal());
                    return dto;
                })
                .toList();
    }


    // ===============================
    // Regla 1: Selección de camión
    // ===============================
    private CamionDTO seleccionarCamionParaSolicitud(Solicitud solicitud) throws ResourceNotFoundException {

        // Obtener lista de camiones desde el microservicio camiones
        List<CamionDTO> disponibles = camionExternalService.obtenerDisponibles();

        if (disponibles == null || disponibles.isEmpty()) {
            throw new RuntimeException("No hay camiones disponibles.");
        }

        // Filtrar por compatibilidad con el contenedor
        return disponibles.stream()
                .filter(c -> c.getCapacidadPeso().compareTo(solicitud.getContenedor().getPeso()) >= 0)
                .filter(c -> c.getCapacidadVolumen().compareTo(solicitud.getContenedor().getVolumen()) >= 0)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No hay camiones que soporten este contenedor."));
    }

    // ===============================
    // Metodo principal: calcular detalles y costos
    // ===============================
    @Transactional
    public SolicitudDetalleDTO calcular(Long idSolicitud) throws ResourceNotFoundException {
        // Armar DTO base
        SolicitudDetalleDTO detalle = buildDetalle(idSolicitud);

        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada"));

        // REGLA 1 -> Obtener camiones compatibles (elegibles)
        List<CamionDTO> camionesElegibles = obtenerCamionesElegibles(solicitud);

        if (camionesElegibles.isEmpty()) {
            log.error("No hay camiones disponibles para la solicitud con ID {}", idSolicitud);
            throw new ResourceNotFoundException("No hay camiones que soporten este contenedor.");
        }

        // REGLA 2 -> Obtener tarifa PROMEDIO entre los camiones elegibles
        TarifaDTO tarifa = obtenerTarifaPromedio(solicitud);

        // Guardamos desglose en el DTO
        detalle.setCostoKilometros(tarifa.getCostoKm());
        detalle.setCostoCombustible(tarifa.getCostoCombustible());
        detalle.setCostoEstadia(tarifa.getCostoEstadiaDeposito());

        log.info("Tarifa obtenida para solicitud con ID {}: Costo Km: {}, Costo Combustible: {}, Costo Estadia: {}",
                idSolicitud, tarifa.getCostoKm(), tarifa.getCostoCombustible(), tarifa.getCostoEstadiaDeposito());

        // Calcular distancia y tiempo por tramos usando OSRM
        calcularDistanciasYTiempo(detalle);

        // ===============================
        // CÁLCULO DE ESTADÍAS REALES
        // ===============================
        long minutosEstadiaReal = calcularEstadiaReal(detalle);
        detalle.setEstadiaRealMinutos(minutosEstadiaReal);

        log.info("Cálculo de estadías reales para solicitud con ID {}: {} minutos", idSolicitud, minutosEstadiaReal);

        // Costo real de estadía basado en lo real
        BigDecimal costoEstadiaReal = calcularCostoEstadiaReal(tarifa, minutosEstadiaReal);

        log.info("Costo real de estadía para solicitud con ID {}: {}", idSolicitud, costoEstadiaReal);

        // Guardar en solicitud
        solicitudRepository.save(solicitud);

        // REGLA 3 - Cálculo del costo total estimado según consigna
        BigDecimal costoEstimado = calcularCostoTotal(detalle, tarifa, costoEstadiaReal);
        detalle.setCostoTotal(costoEstimado);

        solicitud.setCostoEstimado(costoEstimado);
        solicitudRepository.save(solicitud);

        log.info("Costo estimado para solicitud con ID {}: {}", idSolicitud, costoEstimado);

        return detalle;
    }

    private TarifaDTO obtenerTarifaPromedio(Solicitud solicitud) {
        BigDecimal peso = solicitud.getContenedor().getPeso();
        BigDecimal volumen = solicitud.getContenedor().getVolumen();
        TarifaDTO tarifa = tarifaExternalService.obtenerTarifaPorRango(peso, volumen);
        log.info("Tarifa obtenida: Km {}, Combustible {}, Estadía {}", tarifa.getCostoKm(), tarifa.getCostoCombustible(), tarifa.getCostoEstadiaDeposito());
        return tarifa;
    }

    private void calcularDistanciasYTiempo(SolicitudDetalleDTO detalle) {
        BigDecimal distanciaTotalKm = BigDecimal.ZERO;
        long tiempoTotalMin = 0L;

        List<Tramo> tramosActualizados = new ArrayList<>();

        for (TramoDTO tramo : detalle.getTramos()){
            BigDecimal distanciaKm = calcularDistanciaReal(tramo.getOrigen(), tramo.getDestino());
            BigDecimal velocidadPromedioKmH = BigDecimal.valueOf(60);
            long minutos = distanciaKm.divide(velocidadPromedioKmH, 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(60)).longValue();

            tramo.setDistanciaKm(distanciaKm);
            tramo.setTiempoEstimadoMinutos(minutos);

            Tramo tramoEntity = tramoRepository.findById(tramo.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tramo no encontrado"));
            tramoEntity.setDistanciaKm(distanciaKm);
            tramoEntity.setTiempoEstimadoMinutos(minutos);
            tramosActualizados.add(tramoEntity);

            distanciaTotalKm = distanciaTotalKm.add(distanciaKm);
            tiempoTotalMin += minutos;
        }

        tramoRepository.saveAll(tramosActualizados);

        detalle.getRuta().setDistanciaTotalKm(distanciaTotalKm);
        detalle.getRuta().setTiempoTotalMinutos(tiempoTotalMin);
        detalle.setTiempoEstimadoMinutos(tiempoTotalMin);

        log.info("Cálculo de costos y tiempos para solicitud con ID {} completado. Distancia Total: {} km, Tiempo Total: {} minutos.",
                detalle.getId(), distanciaTotalKm, tiempoTotalMin);
    }

    private long calcularEstadiaReal(SolicitudDetalleDTO detalle) {
        return detalle.getTramos().stream()
                .filter(t -> Arrays.asList(1, 2, 3).contains(t.getTipoTramo().getId()))
                .filter(t -> t.getFechaHoraInicio() != null && t.getFechaHoraFin() != null)
                .mapToLong(t -> Duration.between(t.getFechaHoraInicio(), t.getFechaHoraFin()).toMinutes())
                .sum();
    }

    private BigDecimal calcularCostoEstadiaReal(TarifaDTO tarifa, long minutosEstadiaReal) {
        BigDecimal horasEstadia = BigDecimal.valueOf(minutosEstadiaReal)
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        BigDecimal costo = tarifa.getCostoEstadiaDeposito().multiply(horasEstadia);
        log.info("Costo real de estadía: {}", costo);
        return costo;
    }

    private BigDecimal calcularCostoTotal(SolicitudDetalleDTO detalle, TarifaDTO tarifa, BigDecimal costoEstadiaReal) {
        BigDecimal distanciaTotalKm = detalle.getRuta().getDistanciaTotalKm();
        long cantidadTramos = detalle.getTramos().size();
        BigDecimal valorGestionPorTramo = BigDecimal.valueOf(1000);
        BigDecimal cargosGestion = valorGestionPorTramo.multiply(BigDecimal.valueOf(cantidadTramos));

        return tarifa.getCostoKm().multiply(distanciaTotalKm)
                .add(tarifa.getCostoCombustible().multiply(distanciaTotalKm))
                .add(costoEstadiaReal)
                .add(cargosGestion);
    }

    private BigDecimal calcularDistanciaReal(Long idOrigen, Long idDestino) {

        var geoOrigen = geolocalizacionExternalService.obtenerCoordenadas(idOrigen);
        var geoDestino = geolocalizacionExternalService.obtenerCoordenadas(idDestino);

        // Si faltan coordenadas → fallback
        if (geoOrigen == null || geoDestino == null) {
            System.out.println("[OSRM] No se pudo obtener coordenadas, usando fallback de 10km");
            return BigDecimal.TEN;
        }

        // Intentar obtener distancia desde OSRM
        BigDecimal distancia = osrmService.obtenerDistanciaKm(
                BigDecimal.valueOf(geoOrigen.getLatitud()),
                BigDecimal.valueOf(geoOrigen.getLongitud()),
                BigDecimal.valueOf(geoDestino.getLatitud()),
                BigDecimal.valueOf(geoDestino.getLongitud())
        );

        // Si OSRM devolvió null, cero o negativo → fallback
        if (distancia == null || distancia.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("[OSRM] Resultado inválido o nulo, usando fallback de 10km");
            return BigDecimal.TEN;
        }

        return distancia;
    }


    private List<CamionDTO> obtenerCamionesElegibles(Solicitud solicitud) {
        List<CamionDTO> disponibles = camionExternalService.obtenerDisponibles();

        if (disponibles == null || disponibles.isEmpty()) {
            throw new ResourceNotFoundException("No hay camiones disponibles.");
        }

        return disponibles.stream()
                .filter(c -> c.getCapacidadPeso().compareTo(solicitud.getContenedor().getPeso()) >= 0)
                .filter(c -> c.getCapacidadVolumen().compareTo(solicitud.getContenedor().getVolumen()) >= 0)
                .toList();
    }

    public SolicitudDetalleDTO asignarCamion(Long idSolicitud, Long idCamion) {
        // 1. Buscar solicitud
        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada"));

        // 2. Obtener camión desde ms-camiones
        Mono<CamionDTO> camion = camionClient.obtenerCamion(idCamion);

        if (camion == null) {
            log.error("Camión con ID {} no encontrado", idCamion);
            throw new ResourceNotFoundException("Camión no encontrado");
        }

        if (!Objects.requireNonNull(camion.block()).isDisponible()) {
            log.warn("El camión con ID {} no está disponible para la asignación", idCamion);
            throw new ResourceNotFoundException("El camión no está disponible", idCamion);
        }

        // 3. Validar capacidad del camión
        Contenedor cont = solicitud.getContenedor();

        if (cont.getPeso().compareTo(Objects.requireNonNull(camion.block()).getCapacidadPeso()) > 0 ||
                cont.getVolumen().compareTo(Objects.requireNonNull(camion.block()).getCapacidadVolumen()) > 0) {
            log.warn("El camión con ID {} no puede transportar el contenedor con peso {} y volumen {}",
                    idCamion, cont.getPeso(), cont.getVolumen());
            throw new ReglaNegocioException("El camión no puede transportar este contenedor");
        }

        // 4. Asignar camión en solicitud
        solicitud.setIdCamion(idCamion);

        // 5. Costo final = costo estimado
        solicitud.setCostoFinal(solicitud.getCostoEstimado());

        // 6. Cambiar estado a "programada" (ID = 2)
        EstadoSolicitud estadoProgramada = estadoSolicitudRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Estado 'programada' no existe"));

        solicitud.setEstadoSolicitud(estadoProgramada);
        solicitudRepository.save(solicitud);

        // 7. Registrar historial
        solicitudEstadoHistorialService.registrar(idSolicitud, 2L);

        // 8. Marcar camión como NO disponible
        camionClient.marcarNoDisponible(idCamion);

        // 9. Log de operación exitosa
        log.info("Camión con ID {} asignado a solicitud con ID {}. Estado cambiado a 'Programada'", idCamion, idSolicitud);

        // 10. Devolver DTO final
        return buildDetalle(idSolicitud);
    }

    @Transactional(readOnly = true)
    public List<HistorialEstadoDTO> obtenerHistorial(Long idSolicitud) {
        if (!solicitudRepository.existsById(idSolicitud)) {
            throw new ResourceNotFoundException("Solicitud no encontrada",  idSolicitud);
        }

        return historialRepository.findByIdSolicitudOrderByFechaRegistroAsc(idSolicitud)
                .stream()
                .map(historialMapper::toDTO)
                .toList();
    }

    @Transactional
    public SolicitudDetalleDTO cambiarEstado(Long idSolicitud, Long nuevoEstadoId) {

        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada", idSolicitud));

        EstadoSolicitud estadoActual = solicitud.getEstadoSolicitud();

        EstadoSolicitudEnum actualEnum = EstadoSolicitudEnum.fromId(estadoActual.getId());
        EstadoSolicitudEnum nuevoEnum = EstadoSolicitudEnum.fromId(nuevoEstadoId);

        // 1. Validar transición
        if (!estadoValidator.puedeCambiar(actualEnum, nuevoEnum)) {
            Set<EstadoSolicitudEnum> permitidosEnum = estadoValidator.getPermitidos(actualEnum);

            // Obtener nombres de estados permitidos
            List<String> nombresPermitidos = permitidosEnum.stream()
                    .map(EstadoSolicitudEnum::name)
                    .toList();

            throw new TransicionInvalidaException(
                    actualEnum.name(),
                    nuevoEnum.name(),
                    nombresPermitidos
            );
        }

        // 2. Obtener entidad estado
        EstadoSolicitud nuevoEstado = estadoSolicitudRepository.findById(nuevoEstadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Estado destino no existe", nuevoEstadoId));

        // 3. Actualizar estado en solicitud
        solicitud.setEstadoSolicitud(nuevoEstado);
        solicitudRepository.save(solicitud);

        // 4. Registrar en historial
        solicitudEstadoHistorialService.registrar(idSolicitud, nuevoEstadoId);

        // 5. Si la solicitud llega a "entregada", liberar camión
        if (nuevoEnum == EstadoSolicitudEnum.ENTREGADA && solicitud.getIdCamion() != null) {
            camionClient.marcarDisponible(solicitud.getIdCamion());
        }

        // 6. Devolver detalle actualizado
        return buildDetalle(idSolicitud);
    }

}
