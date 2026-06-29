package com.hotel.system.service.estadia;

import com.hotel.system.domain.dto.request.CheckoutRequest;
import com.hotel.system.domain.dto.request.EstadiaRequest;
import com.hotel.system.domain.dto.response.EstadiaResponse;
import com.hotel.system.domain.entity.Cliente;
import com.hotel.system.domain.entity.Estadia;
import com.hotel.system.domain.entity.Habitacion;
import com.hotel.system.domain.entity.TurnoEntidad;
import com.hotel.system.domain.enums.EstadoEstadia;
import com.hotel.system.domain.enums.EstadoHabitacion;
import com.hotel.system.domain.enums.ModoUso;
import com.hotel.system.exception.BusinessException;
import com.hotel.system.mapper.EstadiaMapper;
import com.hotel.system.repository.EstadiaRepository;
import com.hotel.system.service.cliente.ClienteService;
import com.hotel.system.service.habitacion.HabitacionService;
import com.hotel.system.service.turno.TurnoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EstadiaService {

    private final EstadiaRepository estadiaRepository;
    private final EstadiaMapper estadiaMapper;
    private final ClienteService clienteService;
    private final HabitacionService habitacionService;
    private final TurnoService turnoService;

    private static final BigDecimal TARIFA_HORA_ADICIONAL = new BigDecimal("5.00");
    private static final int MAX_HORAS_EXTRA = 3;

    public EstadiaResponse realizarCheckIn(EstadiaRequest request) {
        log.info("Iniciando check-in para cliente: {}, habitación: {}", request.getClienteId(), request.getHabitacionId());

        // 1. Validar cliente
        Cliente cliente = clienteService.obtenerClienteEntity(request.getClienteId());
        if (cliente.getEsProblematico()) {
            throw new BusinessException("El cliente está marcado como problemático. Motivo: " + cliente.getMotivoProblema());
        }

        // 2. Validar habitación
        Habitacion habitacion = habitacionService.obtenerHabitacionEntity(request.getHabitacionId());
        if (habitacion.getEstado() != EstadoHabitacion.DISPONIBLE) {
            throw new BusinessException("La habitación no está disponible actualmente. Estado: " + habitacion.getEstado());
        }

        // 3. Validar que no tenga una estadía activa
        if (estadiaRepository.findActivaByHabitacion(habitacion.getId()).isPresent()) {
            throw new BusinessException("La habitación ya tiene una estadía activa");
        }

        // 4. Obtener turno activo
        TurnoEntidad turno = turnoService.obtenerTurnoActivoEntity();

        // 5. Calcular tarifa inicial
        BigDecimal tarifa = calcularTarifa(habitacion, request);

        // 6. Crear estadía
        Estadia estadia = estadiaMapper.toEntity(request);
        estadia.setCliente(cliente);
        estadia.setHabitacion(habitacion);
        estadia.setTurno(turno);
        estadia.setTarifaAplicada(tarifa);
        estadia.setFechaHoraInicio(LocalDateTime.now());

        estadia = estadiaRepository.save(estadia);

        // 7. Actualizar estado de la habitación
        habitacionService.actualizarEstado(habitacion.getId(), EstadoHabitacion.OCUPADA);

        log.info("Check-in completado. Estadía ID: {}", estadia.getId());

        return estadiaMapper.toResponse(estadia);
    }

    private BigDecimal calcularTarifa(Habitacion habitacion, EstadiaRequest request) {
        if (request.getModoUso() == ModoUso.HORAS) {
            return habitacion.getPrecioPorHoras();
        } else {
            return habitacion.getPrecioPorNoche();
        }
    }

    public EstadiaResponse realizarCheckOut(CheckoutRequest request) {
        log.info("Iniciando check-out para estadía: {}", request.getEstadiaId());

        Estadia estadia = estadiaRepository.findById(request.getEstadiaId())
                .orElseThrow(() -> new BusinessException("Estadía no encontrada con ID: " + request.getEstadiaId()));

        if (estadia.getEstado() != EstadoEstadia.ACTIVA) {
            throw new BusinessException("La estadía no está activa. Estado actual: " + estadia.getEstado());
        }

        // 1. Calcular total
        BigDecimal total = calcularTotalEstadia(estadia);

        // 2. Aplicar descuento si existe
        if (request.getDescuento() != null && request.getDescuento().compareTo(BigDecimal.ZERO) > 0) {
            if (request.getDescuento().compareTo(total) > 0) {
                throw new BusinessException("El descuento no puede ser mayor al total");
            }
            total = total.subtract(request.getDescuento());
        }

        // 3. Actualizar estadía
        estadia.setFechaHoraFin(LocalDateTime.now());
        estadia.setTotal(total);
        estadia.setEstado(EstadoEstadia.FINALIZADA);

        estadia = estadiaRepository.save(estadia);

        // 4. Liberar habitación (poner en limpieza)
        habitacionService.actualizarEstado(estadia.getHabitacion().getId(), EstadoHabitacion.LIMPIEZA);

        log.info("Check-out completado. Estadía ID: {}, Total: {}", estadia.getId(), total);

        return estadiaMapper.toResponse(estadia);
    }

    private BigDecimal calcularTotalEstadia(Estadia estadia) {
        LocalDateTime inicio = estadia.getFechaHoraInicio();
        LocalDateTime fin = LocalDateTime.now(); // Hora actual para check-out

        if (estadia.getModoUso() == ModoUso.HORAS) {
            return calcularTarifaPorHoras(estadia, inicio, fin);
        } else {
            return calcularTarifaPorNoches(estadia, inicio, fin);
        }
    }

    private BigDecimal calcularTarifaPorHoras(Estadia estadia, LocalDateTime inicio, LocalDateTime fin) {
        Duration duracion = Duration.between(inicio, fin);
        long horas = duracion.toHours();

        // Si es menos de 1 hora, cobrar 1 hora
        if (horas == 0 && duracion.toMinutes() > 0) {
            horas = 1;
        }

        BigDecimal tarifaBase = estadia.getTarifaAplicada();

        // Primera hora: tarifa base
        if (horas <= 1) {
            return tarifaBase;
        }

        // Horas adicionales con recargo
        long horasExtras = horas - 1;
        BigDecimal total = tarifaBase;

        // Primeras horas extras: tarifa normal * 1.5
        if (horasExtras <= MAX_HORAS_EXTRA) {
            total = total.add(tarifaBase.multiply(new BigDecimal("1.5")).multiply(new BigDecimal(horasExtras)));
        } else {
            // Más de 3 horas extra, considerar como noche
            total = total.add(tarifaBase.multiply(new BigDecimal("1.5")).multiply(new BigDecimal(MAX_HORAS_EXTRA)));
            long horasRestantes = horasExtras - MAX_HORAS_EXTRA;

            // Cobrar las horas restantes al precio por hora de la habitación
            BigDecimal precioHora = estadia.getHabitacion().getPrecioPorHoras();
            total = total.add(precioHora.multiply(new BigDecimal(horasRestantes)));
        }

        return total;
    }

    private BigDecimal calcularTarifaPorNoches(Estadia estadia, LocalDateTime inicio, LocalDateTime fin) {
        Duration duracion = Duration.between(inicio, fin);
        long horas = duracion.toHours();

        // Si es menos de 12 horas, considerar como medio día
        if (horas < 12) {
            return estadia.getTarifaAplicada().divide(new BigDecimal("2"), BigDecimal.ROUND_HALF_UP);
        }

        // Calcular noches completas
        long noches = horas / 24;

        // Si hay horas extra (más de 12 horas adicionales), cobrar noche extra
        long horasExtras = horas % 24;
        if (horasExtras >= 12) {
            noches++;
        } else if (horasExtras > 0) {
            // Menos de 12 horas, cobrar media noche
            BigDecimal mediaNoche = estadia.getTarifaAplicada().divide(new BigDecimal("2"), BigDecimal.ROUND_HALF_UP);
            BigDecimal nochesBase = estadia.getTarifaAplicada().multiply(new BigDecimal(noches));
            return nochesBase.add(mediaNoche);
        }

        return estadia.getTarifaAplicada().multiply(new BigDecimal(noches));
    }

    @Transactional(readOnly = true)
    public EstadiaResponse obtenerEstadia(Long id) {
        Estadia estadia = estadiaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Estadía no encontrada con ID: " + id));
        return estadiaMapper.toResponse(estadia);
    }

    @Transactional(readOnly = true)
    public List<EstadiaResponse> listarEstadiasActivas() {
        return estadiaRepository.findByEstado(EstadoEstadia.ACTIVA).stream()
                .map(estadiaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EstadiaResponse> listarHistorialCliente(Long clienteId) {
        return estadiaRepository.findByClienteId(clienteId).stream()
                .map(estadiaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EstadiaResponse> listarTodas() {
        return estadiaRepository.findAll().stream()
                .map(estadiaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long contarEstadiasActivas() {
        return estadiaRepository.countActivas();
    }
}