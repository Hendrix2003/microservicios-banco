package com.devsu.cuenta.infrastructure.rest;

import com.devsu.cuenta.application.CuentaService;
import com.devsu.cuenta.dto.CuentaRequest;
import com.devsu.cuenta.dto.CuentaResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    @GetMapping
    public List<CuentaResponse> listar() {
        return cuentaService.listar();
    }

    @GetMapping("/{numeroCuenta}")
    public CuentaResponse obtener(@PathVariable String numeroCuenta) {
        return cuentaService.obtener(numeroCuenta);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CuentaResponse crear(@Valid @RequestBody CuentaRequest req) {
        return cuentaService.crear(req);
    }

    @PutMapping("/{numeroCuenta}")
    public CuentaResponse actualizar(@PathVariable String numeroCuenta,
                                     @Valid @RequestBody CuentaRequest req) {
        return cuentaService.actualizar(numeroCuenta, req);
    }
}
