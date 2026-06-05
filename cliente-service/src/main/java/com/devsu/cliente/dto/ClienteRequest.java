package com.devsu.cliente.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ClienteRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String genero;

    @Min(value = 0, message = "La edad no puede ser negativa")
    private Integer edad;

    @NotBlank(message = "La identificacion es obligatoria")
    private String identificacion;

    private String direccion;

    private String telefono;

    @NotBlank(message = "El clienteId es obligatorio")
    private String clienteId;

    @NotBlank(message = "La contrasena es obligatoria")
    private String contrasena;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
}
