package com.pilshikov.io.ylab.intensive.lesson05.eventsourcing.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.pilshikov.io.ylab.intensive.lesson05.eventsourcing.Person;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class PersonMessagesSender {
    private final ConnectionFactory connectionFactory;

    private final String exchangeName = "api_exc";

    private final String queueName = "api_queue";

    @Autowired
    public PersonMessagesSender(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }


    // Метод отправляет сообщение с содержимым message в очередь
    private void send(String message) throws IOException, TimeoutException {
        try(Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchangeName, "*");

            channel.basicPublish(exchangeName, "*", null, message.getBytes());
        }
    }

    // Метод отправляет сообщение о сохранении объекта Person
    public void sendSaveMessage(Person person) throws IOException, TimeoutException {
        String message = makeSaveMessage(person);

        send(message);
    }

    // Метод отправляет сообщение об удалении объекта Person
    public void sendDeleteMessage(Long id) throws IOException, TimeoutException {
        String message = makeDeleteMessage(id);

        send(message);

    }

    // Метод формирует json для сообщения на сохранение
    private String makeSaveMessage(Person person) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode message = mapper.createObjectNode();
        message.put("operation", "save");
        message.put("object", mapper.writeValueAsString(person));
        return message.toString();
    }

    // Метод формирует json для сообщения на удаление
    private String makeDeleteMessage(Long id) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode message = mapper.createObjectNode();
        message.put("operation", "delete");
        message.put("id", id);

        return message.toString();
    }
}
