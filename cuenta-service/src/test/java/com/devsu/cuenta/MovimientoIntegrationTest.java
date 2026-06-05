package com.devsu.cuenta;

import com.devsu.cuenta.application.MovimientoService;
import com.devsu.cuenta.domain.model.ClienteRef;
import com.devsu.cuenta.domain.model.Cuenta;
import com.devsu.cuenta.domain.repository.ClienteRefRepository;
import com.devsu.cuenta.domain.repository.CuentaRepository;
import com.devsu.cuenta.dto.MovimientoRequest;
import com.devsu.cuenta.dto.MovimientoResponse;
import com.devsu.cuenta.exception.ReglaNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

// Prueba de integracion del registro de movimientos (usa H2 en memoria).
@SpringBootTest(properties =
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration")
@ActiveProfiles("test")
class MovimientoIntegrationTest {

    @Autowired
    private MovimientoService movimientoService;
    @Autowired
    private CuentaRepository cuentaRepository;
    @Autowired
    private ClienteRefRepository clienteRefRepository;

    @BeforeEach
    void preparar() {
        cuentaRepository.deleteAll();
        clienteRefRepository.deleteAll();

        ClienteRef cliente = new ClienteRef();
        cliente.setClienteId("cramirez");
        cliente.setNombre("Carlos Ramirez");
        cliente.setIdentificacion("1700000001");
        cliente.setEstado(true);
        clienteRefRepository.save(cliente);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("478758");
        cuenta.setTipoCuenta("Ahorros");
        cuenta.setSaldoInicial(new BigDecimal("2000"));
        cuenta.setSaldoDisponible(new BigDecimal("2000"));
        cuenta.setEstado(true);
        cuenta.setClienteId("cramirez");
        cuentaRepository.save(cuenta);
    }

    @Test
    @DisplayName("Un retiro valido actualiza el saldo disponible de la cuenta")
    void retiroValidoActualizaSaldo() {
        MovimientoRequest req = new MovimientoRequest();
        req.setNumeroCuenta("478758");
        req.setValor(new BigDecimal("-575"));

        MovimientoResponse resp = movimientoService.crear(req);

        assertEquals(0, resp.getSaldo().compareTo(new BigDecimal("1425")));
        assertEquals("Retiro", resp.getTipoMovimiento());

        Cuenta cuenta = cuentaRepository.findByNumeroCuenta("478758").orElseThrow();
        assertEquals(0, cuenta.getSaldoDisponible().compareTo(new BigDecimal("1425")));
    }

    @Test
    @DisplayName("Un retiro mayor al saldo lanza 'Saldo no disponible'")
    void retiroSinSaldoLanzaError() {
        MovimientoRequest req = new MovimientoRequest();
        req.setNumeroCuenta("478758");
        req.setValor(new BigDecimal("-5000"));

        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class,
                () -> movimientoService.crear(req));
        assertEquals("Saldo no disponible", ex.getMessage());
    }
}
