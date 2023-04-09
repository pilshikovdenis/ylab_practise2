package com.pilshikov.io.ylab.intensive.lesson05.eventsourcing.api;



import com.pilshikov.io.ylab.intensive.lesson05.eventsourcing.Person;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Тут пишем реализацию
 */
@Component
public class PersonApiImpl implements PersonApi {
  private final PersonRepository personRepository;
  private final PersonMessagesSender personMessagesSender;


  public PersonApiImpl(ConnectionFactory connectionFactory, DataSource dataSource) {
    personRepository = new PersonRepository(dataSource);
    personMessagesSender = new PersonMessagesSender(connectionFactory);
  }

  @Override
  public void deletePerson(Long personId) {
    try {
      personMessagesSender.sendDeleteMessage(personId);
    } catch (IOException | TimeoutException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  public void savePerson(Long personId, String firstName, String lastName, String middleName) {
    try {
      personMessagesSender.sendSaveMessage(new Person(personId, firstName, lastName, middleName));
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
