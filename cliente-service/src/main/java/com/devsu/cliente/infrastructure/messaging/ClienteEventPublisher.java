package com.devsu.cliente.infrastructure.messaging;

import com.devsu.cliente.dto.ClienteEvent;
import com.devsu.cliente.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClienteEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publicar(ClienteEvent evento) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                evento);
        log.info("Evento de cliente publicado: tipo={} clienteId={}",
                evento.getTipo(), evento.getClienteId());
    }
}
