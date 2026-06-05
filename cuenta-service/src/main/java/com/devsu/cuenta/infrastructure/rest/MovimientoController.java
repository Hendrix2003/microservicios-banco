package com.devsu.cuenta.infrastructure.rest;

import com.devsu.cuenta.application.MovimientoService;
import com.devsu.cuenta.dto.MovimientoRequest;
import com.devsu.cuenta.dto.MovimientoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @GetMapping
    public List<MovimientoResponse> listar() {
        return movimientoService.listar();
    }

    @GetMapping("/{id}")
    public MovimientoResponse obtener(@PathVariable Long id) {
        return movimientoService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoResponse crear(@Valid @RequestBody MovimientoRequest req) {
        return movimientoService.crear(req);
    }

    @PutMapping("/{id}")
    public MovimientoResponse actualizar(@PathVariable Long id,
                                         @Valid @RequestBody MovimientoRequest req) {
        return movimientoService.actualizar(id, req);
    }
}
