package com.pilshikov.io.ylab.intensive.lesson05.eventsourcing.db;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pilshikov.io.ylab.intensive.lesson05.eventsourcing.Person;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;


@Component
public class PersonMessageReceiver {
    private final String queueName = "api_queue";

    private ConnectionFactory connectionFactory;
    private PersonRepository personRepository;


    public PersonMessageReceiver(ConnectionFactory connectionFactory, PersonRepository personRepository) {
        this.connectionFactory = connectionFactory;
        this.personRepository = personRepository;
    }

    public void processOneMessage() throws IOException, TimeoutException, SQLException {
        // Открываем подключение к RabbitMQ и открываем канал
        try(Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel()) {
            // получаем сообщение из очереди
            GetResponse message = channel.basicGet(queueName, true);
            if (message != null) {
                // вынимаем сообщение из очереди
                String recieved = new String(message.getBody());

                // получаем тип операции из json
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(recieved);
                String operationType = node.get("operation").asText();

                if (operationType.equals("delete")) {
                    // удаление
                    long id = node.get("id").asLong();
                    if (personRepository.find(id) == null) {
                        System.out.println("Попытка удалить несуществующего Person c id = " + id);
                    } else {
                        personRepository.delete(id);
                    }
                } else {
                    // сохранение
                    String object = node.get("object").asText();
                    Person person = mapper.readValue(object, Person.class);
                    personRepository.createOrUpdate(person);
                }
            }
        }
    }

}
