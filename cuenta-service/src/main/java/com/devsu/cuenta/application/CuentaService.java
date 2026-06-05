package com.devsu.cuenta.application;

import com.devsu.cuenta.domain.model.Cuenta;
import com.devsu.cuenta.domain.repository.ClienteRefRepository;
import com.devsu.cuenta.domain.repository.CuentaRepository;
import com.devsu.cuenta.dto.CuentaRequest;
import com.devsu.cuenta.dto.CuentaResponse;
import com.devsu.cuenta.exception.RecursoNoEncontradoException;
import com.devsu.cuenta.exception.ReglaNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRefRepository clienteRefRepository;

    @Transactional(readOnly = true)
    public List<CuentaResponse> listar() {
        return cuentaRepository.findAll().stream().map(this::aResponse).toList();
    }

    @Transactional(readOnly = true)
    public CuentaResponse obtener(String numeroCuenta) {
        return aResponse(buscar(numeroCuenta));
    }

    @Transactional
    public CuentaResponse crear(CuentaRequest req) {
        if (cuentaRepository.existsByNumeroCuenta(req.getNumeroCuenta())) {
            throw new ReglaNegocioException(
                    "Ya existe una cuenta con numero " + req.getNumeroCuenta());
        }
        // valida que el cliente exista en la copia local
        if (!clienteRefRepository.existsById(req.getClienteId())) {
            throw new ReglaNegocioException(
                    "El cliente " + req.getClienteId() + " no esta registrado");
        }
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(req.getNumeroCuenta());
        cuenta.setTipoCuenta(req.getTipoCuenta());
        cuenta.setSaldoInicial(req.getSaldoInicial());
        cuenta.setSaldoDisponible(req.getSaldoInicial());
        cuenta.setEstado(req.getEstado());
        cuenta.setClienteId(req.getClienteId());
        return aResponse(cuentaRepository.save(cuenta));
    }

    @Transactional
    public CuentaResponse actualizar(String numeroCuenta, CuentaRequest req) {
        Cuenta cuenta = buscar(numeroCuenta);
        cuenta.setTipoCuenta(req.getTipoCuenta());
        cuenta.setEstado(req.getEstado());
        return aResponse(cuentaRepository.save(cuenta));
    }

    private Cuenta buscar(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cuenta no encontrada: " + numeroCuenta));
    }

    private CuentaResponse aResponse(Cuenta c) {
        return CuentaResponse.builder()
                .id(c.getId())
                .numeroCuenta(c.getNumeroCuenta())
                .tipoCuenta(c.getTipoCuenta())
                .saldoInicial(c.getSaldoInicial())
                .saldoDisponible(c.getSaldoDisponible())
                .estado(c.getEstado())
                .clienteId(c.getClienteId())
                .build();
    }
}
