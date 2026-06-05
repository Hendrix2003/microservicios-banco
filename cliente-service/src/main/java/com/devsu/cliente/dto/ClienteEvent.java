package com.devsu.cliente.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteEvent implements Serializable {
    private String tipo;
    private String clienteId;
    private String nombre;
    private String identificacion;
    private Boolean estado;
}
