package com.hotel.system.domain.dto.response;

import com.hotel.system.domain.enums.EstadoTurno;
import com.hotel.system.domain.enums.Turno;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TurnoResponse {
    private Long id;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private Turno tipoTurno;
    private EstadoTurno estado;
    private Long empleadoId;
    private String empleadoNombre;
}