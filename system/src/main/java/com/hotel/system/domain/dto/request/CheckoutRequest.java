package com.hotel.system.domain.dto.request;

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
public class CheckoutRequest {

    @NotNull(message = "El ID de la estadía es obligatorio")
    @Positive(message = "El ID de la estadía debe ser positivo")
    private Long estadiaId;

    // Opcional: si se quiere especificar un descuento manual
    private BigDecimal descuento;
}