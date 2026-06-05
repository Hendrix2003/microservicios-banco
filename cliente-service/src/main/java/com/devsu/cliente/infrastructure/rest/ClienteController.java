package com.devsu.cliente.infrastructure.rest;

import com.devsu.cliente.application.ClienteService;
import com.devsu.cliente.dto.ClienteRequest;
import com.devsu.cliente.dto.ClienteResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public List<ClienteResponse> listar() {
        return clienteService.listar();
    }

    @GetMapping("/{clienteId}")
    public ClienteResponse obtener(@PathVariable String clienteId) {
        return clienteService.obtener(clienteId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse crear(@Valid @RequestBody ClienteRequest req) {
        return clienteService.crear(req);
    }

    @PutMapping("/{clienteId}")
    public ClienteResponse actualizar(@PathVariable String clienteId,
                                      @Valid @RequestBody ClienteRequest req) {
        return clienteService.actualizar(clienteId, req);
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Void> eliminar(@PathVariable String clienteId) {
        clienteService.eliminar(clienteId);
        return ResponseEntity.noContent().build();
    }
}
