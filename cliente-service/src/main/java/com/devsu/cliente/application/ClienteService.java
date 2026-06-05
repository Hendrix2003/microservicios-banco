package com.devsu.cliente.application;

import com.devsu.cliente.domain.model.Cliente;
import com.devsu.cliente.domain.repository.ClienteRepository;
import com.devsu.cliente.dto.ClienteEvent;
import com.devsu.cliente.dto.ClienteRequest;
import com.devsu.cliente.dto.ClienteResponse;
import com.devsu.cliente.exception.RecursoNoEncontradoException;
import com.devsu.cliente.exception.ReglaNegocioException;
import com.devsu.cliente.infrastructure.messaging.ClienteEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream().map(this::aResponse).toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse obtener(String clienteId) {
        return aResponse(buscar(clienteId));
    }

    @Transactional
    public ClienteResponse crear(ClienteRequest req) {
        if (clienteRepository.existsByClienteId(req.getClienteId())) {
            throw new ReglaNegocioException("Ya existe un cliente con clienteId " + req.getClienteId());
        }
        if (clienteRepository.existsByIdentificacion(req.getIdentificacion())) {
            throw new ReglaNegocioException("Ya existe un cliente con identificacion " + req.getIdentificacion());
        }
        Cliente cliente = new Cliente();
        aplicar(cliente, req);
        Cliente guardado = clienteRepository.save(cliente);
        publicar("CREATED", guardado);
        return aResponse(guardado);
    }

    @Transactional
    public ClienteResponse actualizar(String clienteId, ClienteRequest req) {
        Cliente cliente = buscar(clienteId);
        aplicar(cliente, req);
        cliente.setClienteId(clienteId);
        Cliente guardado = clienteRepository.save(cliente);
        publicar("UPDATED", guardado);
        return aResponse(guardado);
    }

    @Transactional
    public void eliminar(String clienteId) {
        Cliente cliente = buscar(clienteId);
        clienteRepository.delete(cliente);
        publicar("DELETED", cliente);
    }

    private Cliente buscar(String clienteId) {
        return clienteRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Cliente no encontrado: " + clienteId));
    }

    private void aplicar(Cliente c, ClienteRequest req) {
        c.setNombre(req.getNombre());
        c.setGenero(req.getGenero());
        c.setEdad(req.getEdad());
        c.setIdentificacion(req.getIdentificacion());
        c.setDireccion(req.getDireccion());
        c.setTelefono(req.getTelefono());
        c.setClienteId(req.getClienteId());
        c.setContrasena(req.getContrasena());
        c.setEstado(req.getEstado());
    }

    private void publicar(String tipo, Cliente c) {
        eventPublisher.publicar(ClienteEvent.builder()
                .tipo(tipo)
                .clienteId(c.getClienteId())
                .nombre(c.getNombre())
                .identificacion(c.getIdentificacion())
                .estado(c.getEstado())
                .build());
    }

    private ClienteResponse aResponse(Cliente c) {
        return ClienteResponse.builder()
                .id(c.getId())
                .clienteId(c.getClienteId())
                .nombre(c.getNombre())
                .genero(c.getGenero())
                .edad(c.getEdad())
                .identificacion(c.getIdentificacion())
                .direccion(c.getDireccion())
                .telefono(c.getTelefono())
                .estado(c.getEstado())
                .build();
    }
}
