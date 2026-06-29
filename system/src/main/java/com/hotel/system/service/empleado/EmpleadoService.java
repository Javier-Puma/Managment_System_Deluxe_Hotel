package com.hotel.system.service.empleado;

import com.hotel.system.domain.entity.Empleado;
import com.hotel.system.domain.enums.Turno;
import com.hotel.system.exception.BusinessException;
import com.hotel.system.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    @Transactional(readOnly = true)
    public Empleado obtenerEmpleadoEntity(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Empleado no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public Empleado obtenerEmpleadoPorDni(String dni) {
        return empleadoRepository.findByDni(dni)
                .orElseThrow(() -> new BusinessException("Empleado no encontrado con DNI: " + dni));
    }

    @Transactional(readOnly = true)
    public boolean existeEmpleadoConTurno(Turno turno) {
        return empleadoRepository.findAll().stream()
                .anyMatch(e -> e.getTurnoAsignado() == turno && e.getActivo());
    }
}