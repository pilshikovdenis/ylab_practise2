package com.pilshikov.io.ylab.intensive.lesson05.eventsourcing.api;


import com.pilshikov.io.ylab.intensive.lesson05.eventsourcing.Person;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class ApiApp {
  public static void main(String[] args) throws Exception {
    // Тут пишем создание PersonApi, запуск и демонстрацию работы
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();
    PersonApi personApi = applicationContext.getBean(PersonApi.class);
    // пишем взаимодействие с PersonApi

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
}
