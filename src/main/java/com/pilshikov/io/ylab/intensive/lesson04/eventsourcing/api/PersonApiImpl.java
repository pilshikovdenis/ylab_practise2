package com.pilshikov.io.ylab.intensive.lesson04.eventsourcing.api;

import com.pilshikov.io.ylab.intensive.lesson04.eventsourcing.Person;
import com.pilshikov.io.ylab.intensive.lesson04.eventsourcing.messages.PersonMessages;
import com.pilshikov.io.ylab.intensive.lesson04.eventsourcing.repository.PersonRepository;
import com.rabbitmq.client.ConnectionFactory;


import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Тут пишем реализацию
 */
public class PersonApiImpl implements PersonApi {
  private final PersonRepository personRepository;
  private final PersonMessages personMessages;

  public PersonApiImpl(ConnectionFactory connectionFactory, DataSource dataSource) {
    personRepository = new PersonRepository(dataSource);
    personMessages = new PersonMessages(connectionFactory);
  }

  @Override
  public void deletePerson(Long personId) {
    try {
      personMessages.sendDeleteMessage(personId);
    } catch (IOException | TimeoutException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  public void savePerson(Long personId, String firstName, String lastName, String middleName) {
    try {
      personMessages.sendSaveMessage(new Person(personId, firstName, lastName, middleName));
    } catch (TimeoutException | IOException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  public Person findPerson(Long personId) {
    try {
      return  personRepository.find(personId);
    } catch (SQLException e) {
      System.out.println(e.getMessage());;
    }
    return null;
  }

  @Override
  public List<Person> findAll() {
    try {
      return personRepository.findAll();
    } catch (SQLException e) {
      System.out.println(e.getMessage());;
    }
    return null;
  }
}
