package com.devsu.cuenta.application;

import com.devsu.cuenta.domain.model.Cuenta;
import com.devsu.cuenta.domain.model.Movimiento;
import com.devsu.cuenta.domain.repository.CuentaRepository;
import com.devsu.cuenta.domain.repository.MovimientoRepository;
import com.devsu.cuenta.dto.MovimientoRequest;
import com.devsu.cuenta.dto.MovimientoResponse;
import com.devsu.cuenta.exception.RecursoNoEncontradoException;
import com.devsu.cuenta.exception.ReglaNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    @Transactional(readOnly = true)
    public List<MovimientoResponse> listar() {
        return movimientoRepository.findAll().stream().map(this::aResponse).toList();
    }

    @Transactional(readOnly = true)
    public MovimientoResponse obtener(Long id) {
        return aResponse(buscar(id));
    }

    @Transactional
    public MovimientoResponse crear(MovimientoRequest req) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(req.getNumeroCuenta())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cuenta no encontrada: " + req.getNumeroCuenta()));

        if (req.getValor() == null || req.getValor().compareTo(BigDecimal.ZERO) == 0) {
            throw new ReglaNegocioException("El valor del movimiento no puede ser cero");
        }

        BigDecimal nuevoSaldo = cuenta.getSaldoDisponible().add(req.getValor());
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new ReglaNegocioException("Saldo no disponible");
        }

        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(req.getFecha() != null ? req.getFecha() : LocalDateTime.now());
        movimiento.setTipoMovimiento(
                req.getValor().compareTo(BigDecimal.ZERO) > 0 ? "Deposito" : "Retiro");
        movimiento.setValor(req.getValor());
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setCuenta(cuenta);

        cuenta.setSaldoDisponible(nuevoSaldo);
        cuentaRepository.save(cuenta);

        return aResponse(movimientoRepository.save(movimiento));
    }

    @Transactional
    public MovimientoResponse actualizar(Long id, MovimientoRequest req) {
        Movimiento movimiento = buscar(id);
        movimiento.setValor(req.getValor());
        movimiento.setTipoMovimiento(
                req.getValor().compareTo(BigDecimal.ZERO) > 0 ? "Deposito" : "Retiro");
        if (req.getFecha() != null) {
            movimiento.setFecha(req.getFecha());
        }
        movimientoRepository.save(movimiento);
        recalcularSaldos(movimiento.getCuenta());
        return aResponse(buscar(id));
    }

    private void recalcularSaldos(Cuenta cuenta) {
        List<Movimiento> movimientos =
                movimientoRepository.findByCuentaIdOrderByFechaAscIdAsc(cuenta.getId());
        BigDecimal saldo = cuenta.getSaldoInicial();
        for (Movimiento m : movimientos) {
            saldo = saldo.add(m.getValor());
            if (saldo.compareTo(BigDecimal.ZERO) < 0) {
                throw new ReglaNegocioException(
                        "Saldo no disponible al recalcular el movimiento " + m.getId());
            }
            m.setSaldo(saldo);
            movimientoRepository.save(m);
        }
        cuenta.setSaldoDisponible(saldo);
        cuentaRepository.save(cuenta);
    }

    private Movimiento buscar(Long id) {
        return movimientoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Movimiento no encontrado: " + id));
    }

    private MovimientoResponse aResponse(Movimiento m) {
        return MovimientoResponse.builder()
                .id(m.getId())
                .fecha(m.getFecha())
                .tipoMovimiento(m.getTipoMovimiento())
                .valor(m.getValor())
                .saldo(m.getSaldo())
                .numeroCuenta(m.getCuenta().getNumeroCuenta())
                .build();
    }
}
