package com.hotel.system.service.habitacion;

import com.hotel.system.domain.dto.response.HabitacionResponse;
import com.hotel.system.domain.entity.Habitacion;
import com.hotel.system.domain.enums.EstadoHabitacion;
import com.hotel.system.domain.enums.TipoHabitacion;
import com.hotel.system.exception.BusinessException;
import com.hotel.system.mapper.HabitacionMapper;
import com.hotel.system.repository.HabitacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final HabitacionMapper habitacionMapper;

    public HabitacionResponse crearHabitacion(HabitacionRequest request) {
        // Verificar si el número ya existe
        if (habitacionRepository.findByNumero(request.getNumero()).isPresent()) {
            throw new BusinessException("Ya existe una habitación con el número: " + request.getNumero());
        }

        Habitacion habitacion = habitacionMapper.toEntity(request);
        habitacion = habitacionRepository.save(habitacion);
        return habitacionMapper.toResponse(habitacion);
    }

    @Transactional(readOnly = true)
    public HabitacionResponse obtenerHabitacion(Long id) {
        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Habitación no encontrada con ID: " + id));
        return habitacionMapper.toResponse(habitacion);
    }

    @Transactional(readOnly = true)
    public List<HabitacionResponse> listarTodas() {
        return habitacionRepository.findAll().stream()
                .map(habitacionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HabitacionResponse> listarDisponibles() {
        return habitacionRepository.findByEstado(EstadoHabitacion.DISPONIBLE).stream()
                .map(habitacionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HabitacionResponse> listarPorTipo(TipoHabitacion tipo) {
        return habitacionRepository.findByTipo(tipo).stream()
                .map(habitacionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HabitacionResponse> listarDisponiblesPorTipo(TipoHabitacion tipo) {
        return habitacionRepository.findDisponiblesByTipo(EstadoHabitacion.DISPONIBLE, tipo).stream()
                .map(habitacionMapper::toResponse)
                .collect(Collectors.toList());
    }

    public HabitacionResponse actualizarHabitacion(Long id, HabitacionRequest request) {
        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Habitación no encontrada con ID: " + id));

        // Verificar número único (si cambió)
        if (!habitacion.getNumero().equals(request.getNumero())) {
            if (habitacionRepository.findByNumero(request.getNumero()).isPresent()) {
                throw new BusinessException("Ya existe una habitación con el número: " + request.getNumero());
            }
        }

        habitacionMapper.updateEntity(habitacion, request);
        habitacion = habitacionRepository.save(habitacion);
        return habitacionMapper.toResponse(habitacion);
    }

    public HabitacionResponse actualizarEstado(Long id, EstadoHabitacion nuevoEstado) {
        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Habitación no encontrada con ID: " + id));

        // Validar transiciones de estado
        validarTransicionEstado(habitacion.getEstado(), nuevoEstado);

        habitacion.setEstado(nuevoEstado);
        habitacion = habitacionRepository.save(habitacion);
        return habitacionMapper.toResponse(habitacion);
    }

    private void validarTransicionEstado(EstadoHabitacion actual, EstadoHabitacion nuevo) {
        // Reglas de negocio para cambios de estado
        if (actual == EstadoHabitacion.MANTENIMIENTO && nuevo != EstadoHabitacion.DISPONIBLE) {
            throw new BusinessException("Una habitación en mantenimiento solo puede pasar a DISPONIBLE");
        }
        if (actual == EstadoHabitacion.OCUPADA && nuevo != EstadoHabitacion.LIMPIEZA) {
            throw new BusinessException("Una habitación ocupada solo puede pasar a LIMPIEZA");
        }
        if (actual == EstadoHabitacion.LIMPIEZA && nuevo != EstadoHabitacion.DISPONIBLE && nuevo != EstadoHabitacion.RESERVADA) {
            throw new BusinessException("Una habitación en limpieza solo puede pasar a DISPONIBLE o RESERVADA");
        }
    }

    public void eliminarHabitacion(Long id) {
        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Habitación no encontrada con ID: " + id));

        if (habitacion.getEstado() == EstadoHabitacion.OCUPADA) {
            throw new BusinessException("No se puede eliminar una habitación que está ocupada");
        }

        habitacionRepository.delete(habitacion);
    }

    @Transactional(readOnly = true)
    public Habitacion obtenerHabitacionEntity(Long id) {
        return habitacionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Habitación no encontrada con ID: " + id));
    }
}