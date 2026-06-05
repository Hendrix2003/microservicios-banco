package com.devsu.cuenta.application;

import com.devsu.cuenta.domain.model.ClienteRef;
import com.devsu.cuenta.domain.model.Movimiento;
import com.devsu.cuenta.domain.repository.ClienteRefRepository;
import com.devsu.cuenta.domain.repository.MovimientoRepository;
import com.devsu.cuenta.dto.ReporteMovimientoDTO;
import com.devsu.cuenta.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("d/M/yyyy");

    private final MovimientoRepository movimientoRepository;
    private final ClienteRefRepository clienteRefRepository;

    @Transactional(readOnly = true)
    public List<ReporteMovimientoDTO> generar(String clienteId,
                                              LocalDate fechaInicio,
                                              LocalDate fechaFin) {
        ClienteRef cliente = clienteRefRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cliente no encontrado: " + clienteId));

        LocalDateTime desde = fechaInicio.atStartOfDay();
        LocalDateTime hasta = fechaFin.atTime(LocalTime.MAX);

        List<Movimiento> movimientos = movimientoRepository
                .findByCuentaClienteIdAndFechaBetweenOrderByFechaAsc(clienteId, desde, hasta);

        return movimientos.stream().map(m -> ReporteMovimientoDTO.builder()
                .fecha(m.getFecha().format(FORMATO))
                .cliente(cliente.getNombre())
                .numeroCuenta(m.getCuenta().getNumeroCuenta())
                .tipo(m.getCuenta().getTipoCuenta())
                .saldoInicial(m.getCuenta().getSaldoInicial())
                .estado(m.getCuenta().getEstado())
                .movimiento(m.getValor())
                .saldoDisponible(m.getSaldo())
                .build()).toList();
    }
}
