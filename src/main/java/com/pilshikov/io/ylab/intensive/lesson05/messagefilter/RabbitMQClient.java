package com.pilshikov.io.ylab.intensive.lesson05.messagefilter;

import com.rabbitmq.client.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class RabbitMQClient {
    private ConnectionFactory connectionFactory;
    private final String inputExchangeName = "messagefilter_input";
    private final String outputExchangeName = "messagefilter_output";
    private final String inputQueueName = "filter_queue_input";
    private final String outputQueueName = "filter_queue_output";

    public RabbitMQClient(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @PostConstruct
    private void initializeSettings() throws IOException, TimeoutException {
        try(Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel()) {

            System.out.println("Инициализация очереди input ");
            channel.exchangeDeclare(inputExchangeName, BuiltinExchangeType.TOPIC);
            channel.queueDeclare(inputQueueName, true, false, false, null);
            channel.queueBind(inputQueueName, inputExchangeName, "*");

            System.out.println("Инициализация очереди output ");
            channel.exchangeDeclare(outputExchangeName, BuiltinExchangeType.TOPIC);
            channel.queueDeclare(outputQueueName, true, false, false, null);
            channel.queueBind(outputQueueName, outputExchangeName, "*");

        }
    }
    public void sendOutputMessage(String message) throws IOException, TimeoutException {
        if (message == null) {
            return;
        }
        try(Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel()) {
            channel.basicPublish(outputExchangeName, "*", null, message.getBytes());
        }
    }

    public String getInputMessage() throws IOException, TimeoutException {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            GetResponse message = channel.basicGet(inputQueueName, true);
            if (message != null) {
                return new String(message.getBody());
            }
            else {
                return null;
            }

        }
    }


}
