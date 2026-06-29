package com.hotel.system.domain.dto.request;

import com.hotel.system.domain.enums.ModoUso;
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
public class EstadiaRequest {

    @NotNull(message = "El ID del cliente es obligatorio")
    @Positive(message = "El ID del cliente debe ser positivo")
    private Long clienteId;

    @NotNull(message = "El ID de la habitación es obligatorio")
    @Positive(message = "El ID de la habitación debe ser positivo")
    private Long habitacionId;

    @NotNull(message = "El modo de uso es obligatorio")
    private ModoUso modoUso;

    // Opcional: si se especifica, se usa para calcular tarifa
    private Integer horasEstimadas;

    // Opcional: si se especifica, se usa para calcular tarifa
    private Integer nochesEstimadas;
}