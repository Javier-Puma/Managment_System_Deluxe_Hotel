package com.hotel.system.domain.dto.request;

import com.hotel.system.domain.enums.Turno;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbrirTurnoRequest {

    @NotNull(message = "El tipo de turno es obligatorio")
    private Turno tipoTurno;

    @NotNull(message = "El ID del empleado es obligatorio")
    @Positive(message = "El ID del empleado debe ser positivo")
    private Long empleadoId;
}