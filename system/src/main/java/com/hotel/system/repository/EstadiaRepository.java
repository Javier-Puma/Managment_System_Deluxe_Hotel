package com.hotel.system.repository;

import com.hotel.system.domain.entity.Estadia;
import com.hotel.system.domain.enums.EstadoEstadia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EstadiaRepository extends JpaRepository<Estadia, Long> {

    List<Estadia> findByEstado(EstadoEstadia estado);

    List<Estadia> findByClienteId(Long clienteId);

    Optional<Estadia> findByHabitacionIdAndEstado(Long habitacionId, EstadoEstadia estado);

    @Query("SELECT e FROM Estadia e WHERE e.habitacion.id = :habitacionId AND e.estado = 'ACTIVA'")
    Optional<Estadia> findActivaByHabitacion(@Param("habitacionId") Long habitacionId);

    @Query("SELECT e FROM Estadia e WHERE e.fechaHoraInicio BETWEEN :inicio AND :fin")
    List<Estadia> findByFechaInicioBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT COUNT(e) FROM Estadia e WHERE e.estado = 'ACTIVA'")
    Long countActivas();
}