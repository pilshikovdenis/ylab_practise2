package com.pilshikov.io.ylab.intensive.lesson04.eventsourcing.api;

import com.pilshikov.io.ylab.intensive.lesson04.DbUtil;
import com.pilshikov.io.ylab.intensive.lesson04.RabbitMQUtil;
import com.pilshikov.io.ylab.intensive.lesson04.eventsourcing.Person;
import com.rabbitmq.client.ConnectionFactory;


import javax.sql.DataSource;
import java.util.List;

public class ApiApp {
  public static void main(String[] args) throws Exception {
    ConnectionFactory connectionFactory = initMQ();
    DataSource dataSource = DbUtil.buildDataSource();

    // Тут пишем создание PersonApi, запуск и демонстрацию работы
    PersonApi personApi = new PersonApiImpl(connectionFactory, dataSource);


    // получаем уже добавленных
    List<Person> persons = personApi.findAll();
    System.out.println(persons);

    personApi.savePerson(1L, "Генадий", "Ветров", "Александрович");
    personApi.savePerson(2L, "Денис", "Павлов", "Аркадьевич");
    personApi.savePerson(3L, "Виктор", "Броницкий", "Андреевич");
    personApi.savePerson(4L, "Олег", "Ленин", "Петрович");

    personApi.deletePerson(3L);
    personApi.deletePerson(50L);

    Person person1 = personApi.findPerson(2L);
    Person person2 = personApi.findPerson(3L);
    System.out.println(person1);
    System.out.println(person2);



  }

  private static ConnectionFactory initMQ() throws Exception {
    return RabbitMQUtil.buildConnectionFactory();
  }
}
