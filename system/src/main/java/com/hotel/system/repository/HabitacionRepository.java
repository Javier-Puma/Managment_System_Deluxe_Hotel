package com.hotel.system.repository;

import com.hotel.system.domain.entity.Habitacion;
import com.hotel.system.domain.enums.EstadoHabitacion;
import com.hotel.system.domain.enums.TipoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
    Optional<Habitacion> findByNumero(String numero);

    List<Habitacion> findByEstado(EstadoHabitacion estado);

    List<Habitacion> findByTipo(TipoHabitacion tipo);

    @Query("SELECT h FROM Habitacion h WHERE h.estado = :estado AND h.tipo = :tipo")
    List<Habitacion> findDisponiblesByTipo(@Param("estado") EstadoHabitacion estado, @Param("tipo") TipoHabitacion tipo);
}