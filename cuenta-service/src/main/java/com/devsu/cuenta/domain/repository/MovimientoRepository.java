package com.devsu.cuenta.domain.repository;

import com.devsu.cuenta.domain.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaIdOrderByFechaAscIdAsc(Long cuentaId);

    List<Movimiento> findByCuentaClienteIdAndFechaBetweenOrderByFechaAsc(
            String clienteId, LocalDateTime desde, LocalDateTime hasta);
}
