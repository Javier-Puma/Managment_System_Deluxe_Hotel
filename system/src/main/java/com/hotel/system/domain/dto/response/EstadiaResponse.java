package com.hotel.system.domain.dto.response;

import com.hotel.system.domain.enums.EstadoEstadia;
import com.hotel.system.domain.enums.ModoUso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadiaResponse {
    private Long id;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private ModoUso modoUso;
    private BigDecimal tarifaAplicada;
    private BigDecimal total;
    private EstadoEstadia estado;

    // Datos del cliente
    private Long clienteId;
    private String clienteNombre;
    private String clienteDni;

    // Datos de la habitación
    private Long habitacionId;
    private String habitacionNumero;
    private String habitacionTipo;

    // Datos del turno
    private Long turnoId;
    private String turnoTipo;
}