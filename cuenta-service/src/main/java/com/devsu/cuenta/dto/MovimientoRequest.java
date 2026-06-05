package com.devsu.cuenta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MovimientoRequest {

    @NotBlank(message = "El numero de cuenta es obligatorio")
    private String numeroCuenta;

    @NotNull(message = "El valor del movimiento es obligatorio")
    private BigDecimal valor;

    private LocalDateTime fecha;
}
