package com.hotel.system.domain.dto.response;

import com.hotel.system.domain.enums.EstadoHabitacion;
import com.hotel.system.domain.enums.TipoHabitacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitacionResponse {
    private Long id;
    private String numero;
    private TipoHabitacion tipo;
    private EstadoHabitacion estado;
    private String descripcion;
    private BigDecimal precioPorNoche;
    private BigDecimal precioPorHoras;
    private String caracteristicas;
}