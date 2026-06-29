package com.hotel.system.service.turno;

import com.hotel.system.domain.entity.Empleado;
import com.hotel.system.domain.entity.TurnoEntidad;
import com.hotel.system.domain.enums.EstadoTurno;
import com.hotel.system.exception.BusinessException;
import com.hotel.system.repository.TurnoRepository;
import com.hotel.system.service.empleado.EmpleadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final EmpleadoService empleadoService;

    public TurnoResponse abrirTurno(AbrirTurnoRequest request) {
        // Verificar si hay un turno abierto
        if (turnoRepository.findByEstado(EstadoTurno.ABIERTO).isPresent()) {
            throw new BusinessException("Ya existe un turno abierto. Debe cerrarlo antes de abrir uno nuevo.");
        }

        Empleado empleado = empleadoService.obtenerEmpleadoEntity(request.getEmpleadoId());

        // Validar que el empleado tenga el turno correcto
        if (empleado.getTurnoAsignado() != request.getTipoTurno()) {
            throw new BusinessException("El empleado no está asignado al turno " + request.getTipoTurno());
        }

        TurnoEntidad turno = TurnoEntidad.builder()
                .fechaHoraInicio(LocalDateTime.now())
                .tipoTurno(request.getTipoTurno())
                .empleado(empleado)
                .estado(EstadoTurno.ABIERTO)
                .build();

        turno = turnoRepository.save(turno);
        return mapToResponse(turno);
    }

    public TurnoResponse cerrarTurno(Long turnoId) {
        TurnoEntidad turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new BusinessException("Turno no encontrado con ID: " + turnoId));

        if (turno.getEstado() == EstadoTurno.CERRADO) {
            throw new BusinessException("El turno ya está cerrado");
        }

        turno.setFechaHoraFin(LocalDateTime.now());
        turno.setEstado(EstadoTurno.CERRADO);
        turno = turnoRepository.save(turno);

        return mapToResponse(turno);
    }

    @Transactional(readOnly = true)
    public TurnoResponse obtenerTurno(Long id) {
        TurnoEntidad turno = turnoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Turno no encontrado con ID: " + id));
        return mapToResponse(turno);
    }

    @Transactional(readOnly = true)
    public TurnoResponse obtenerTurnoActivo() {
        TurnoEntidad turno = turnoRepository.findByEstado(EstadoTurno.ABIERTO)
                .orElseThrow(() -> new BusinessException("No hay un turno activo"));
        return mapToResponse(turno);
    }

    @Transactional(readOnly = true)
    public List<TurnoResponse> listarTurnos() {
        return turnoRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TurnoResponse> listarTurnosPorEmpleado(Long empleadoId) {
        return turnoRepository.findByEmpleadoId(empleadoId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TurnoEntidad obtenerTurnoActivoEntity() {
        return turnoRepository.findByEstado(EstadoTurno.ABIERTO)
                .orElseThrow(() -> new BusinessException("No hay un turno activo"));
    }

    @Transactional(readOnly = true)
    public TurnoEntidad obtenerTurnoEntity(Long id) {
        return turnoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Turno no encontrado con ID: " + id));
    }

    private TurnoResponse mapToResponse(TurnoEntidad turno) {
        return TurnoResponse.builder()
                .id(turno.getId())
                .fechaHoraInicio(turno.getFechaHoraInicio())
                .fechaHoraFin(turno.getFechaHoraFin())
                .tipoTurno(turno.getTipoTurno())
                .estado(turno.getEstado())
                .empleadoId(turno.getEmpleado().getId())
                .empleadoNombre(turno.getEmpleado().getNombreCompleto())
                .build();
    }
}