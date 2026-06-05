package com.devsu.cuenta.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cliente_ref")
@Getter
@Setter
@NoArgsConstructor
public class ClienteRef {

    @Id
    @Column(name = "cliente_id")
    private String clienteId;

    private String nombre;

    private String identificacion;

    private Boolean estado;
}
