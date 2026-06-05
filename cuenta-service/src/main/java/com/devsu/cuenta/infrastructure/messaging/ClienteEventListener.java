package com.devsu.cuenta.infrastructure.messaging;

import com.devsu.cuenta.domain.model.ClienteRef;
import com.devsu.cuenta.domain.repository.ClienteRefRepository;
import com.devsu.cuenta.dto.ClienteEvent;
import com.devsu.cuenta.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

// Escucha los eventos de cliente y actualiza la tabla cliente_ref.
@Slf4j
@Component
@RequiredArgsConstructor
public class ClienteEventListener {

    private final ClienteRefRepository clienteRefRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void recibir(ClienteEvent evento) {
        log.info("Evento de cliente recibido: tipo={} clienteId={}",
                evento.getTipo(), evento.getClienteId());

        if ("DELETED".equals(evento.getTipo())) {
            clienteRefRepository.deleteById(evento.getClienteId());
            return;
        }

        ClienteRef ref = clienteRefRepository.findById(evento.getClienteId())
                .orElseGet(ClienteRef::new);
        ref.setClienteId(evento.getClienteId());
        ref.setNombre(evento.getNombre());
        ref.setIdentificacion(evento.getIdentificacion());
        ref.setEstado(evento.getEstado());
        clienteRefRepository.save(ref);
    }
}
