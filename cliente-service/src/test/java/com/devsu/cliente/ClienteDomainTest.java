package com.devsu.cliente;

import com.devsu.cliente.domain.model.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteDomainTest {

    @Test
    @DisplayName("Cliente hereda atributos de Persona y conserva los propios")
    void clienteHeredaDePersona() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Carlos Ramirez");
        cliente.setGenero("M");
        cliente.setEdad(30);
        cliente.setIdentificacion("1700000001");
        cliente.setDireccion("Av. Principal 123");
        cliente.setTelefono("0991234567");
        cliente.setClienteId("cramirez");
        cliente.setContrasena("1234");
        cliente.setEstado(true);

        assertEquals("Carlos Ramirez", cliente.getNombre());
        assertEquals("1700000001", cliente.getIdentificacion());
        assertEquals("cramirez", cliente.getClienteId());
        assertEquals("1234", cliente.getContrasena());
        assertTrue(cliente.getEstado());
    }

    @Test
    @DisplayName("Cliente es una instancia de Persona")
    void clienteEsPersona() {
        Cliente cliente = new Cliente();
        assertInstanceOf(com.devsu.cliente.domain.model.Persona.class, cliente);
    }
}
