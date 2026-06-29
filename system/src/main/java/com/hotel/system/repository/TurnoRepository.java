package com.hotel.system.repository;

import com.hotel.system.domain.entity.TurnoEntidad;
import com.hotel.system.domain.enums.EstadoTurno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TurnoRepository extends JpaRepository<TurnoEntidad, Long> {
    Optional<TurnoEntidad> findByEstado(EstadoTurno estado);
    List<TurnoEntidad> findByEmpleadoId(Long empleadoId);
}