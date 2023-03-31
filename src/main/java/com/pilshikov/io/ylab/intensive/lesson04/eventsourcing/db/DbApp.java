package com.pilshikov.io.ylab.intensive.lesson04.eventsourcing.db;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pilshikov.io.ylab.intensive.lesson04.DbUtil;
import com.pilshikov.io.ylab.intensive.lesson04.RabbitMQUtil;
import com.pilshikov.io.ylab.intensive.lesson04.eventsourcing.Person;
import com.pilshikov.io.ylab.intensive.lesson04.eventsourcing.repository.PersonRepository;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;


import javax.sql.DataSource;
import java.sql.SQLException;

public class DbApp {

  public static void main(String[] args) throws Exception {
    String exchangeName = "api_exc";
    String queueName = "api_queue";

    DataSource dataSource = initDb();
    ConnectionFactory connectionFactory = initMQ();

    // тут пишем создание и запуск приложения работы с БД
    PersonRepository personRepository = new PersonRepository(dataSource);

    // Открываем подключение к RabbitMQ и открываем канал
    try(Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel()) {
      // Пока поток запущен получаем сообщения из очереди
      while (!Thread.currentThread().isInterrupted()) {
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
  
  private static ConnectionFactory initMQ() throws Exception {
    return RabbitMQUtil.buildConnectionFactory();
  }
  
  private static DataSource initDb() throws SQLException {
    String ddl = "" 
                     + "drop table if exists person;" 
                     + "create table if not exists person (\n"
                     + "person_id bigint primary key,\n"
                     + "first_name varchar,\n"
                     + "last_name varchar,\n"
                     + "middle_name varchar\n"
                     + ")";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(ddl, dataSource);
    return dataSource;
  }
}
