package com.hotel.system.domain.dto.request;

import com.hotel.system.domain.enums.TipoHabitacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitacionRequest {

    @NotBlank(message = "El número de habitación es obligatorio")
    private String numero;

    @NotNull(message = "El tipo de habitación es obligatorio")
    private TipoHabitacion tipo;

    private String descripcion;

    @NotNull(message = "El precio por noche es obligatorio")
    @Positive(message = "El precio por noche debe ser mayor a 0")
    private BigDecimal precioPorNoche;

    @Positive(message = "El precio por horas debe ser mayor a 0")
    private BigDecimal precioPorHoras;

    private String caracteristicas;
}