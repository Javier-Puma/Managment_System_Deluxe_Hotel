package com.hotel.system.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponse {
    private Long id;
    private String dni;
    private String nombreCompleto;
    private String telefono;
    private String email;
    private Boolean esProblematico;
    private String motivoProblema;
    private LocalDateTime fechaRegistro;
}