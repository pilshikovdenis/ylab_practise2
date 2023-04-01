package com.pilshikov.io.ylab.intensive.lesson05.eventsourcing.db;



import com.pilshikov.io.ylab.intensive.lesson05.eventsourcing.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonRepository {
    private DataSource dataSource;

    @Autowired
    public PersonRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void createOrUpdate(Person person) throws SQLException {
        String insertQuery = "INSERT INTO person (person_id, first_name, last_name, middle_name) VALUES(?, ?, ?, ?);";
        String updateQuery = "UPDATE person SET first_name=?, last_name=?, middle_name=? WHERE person_id=?;";
        String selectQuery = "SELECT first_name FROM person WHERE person_id = ?;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            // проверяем существует ли запись с таким id
            selectStatement.setLong(1, person.getId());
            ResultSet rs = selectStatement.executeQuery();
            boolean recordExists = rs.isBeforeFirst();
            rs.close();

            if (recordExists) {
                // запрос на обновление
                updateStatement.setString(1, person.getName());
                updateStatement.setString(2, person.getLastName());
                updateStatement.setString(3, person.getMiddleName());
                updateStatement.setLong(4, person.getId());
                updateStatement.executeUpdate();

            } else {
                // запрос на добавление
                insertStatement.setLong(1, person.getId());
                insertStatement.setString(2, person.getName());
                insertStatement.setString(3, person.getLastName());
                insertStatement.setString(4, person.getMiddleName());
                insertStatement.executeUpdate();

            }
        }
    }

    public void delete(Long id) throws SQLException {
        String deleteQuery = "DELETE FROM person WHERE person_id=?;";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.setLong(1, id);
            deleteStatement.execute();
        }
    }

    public Person find(Long id) throws SQLException {
        String selectQuery = "SELECT * FROM person WHERE person_id = ?;";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if(!rs.isBeforeFirst()) {
                return null;
            }
            Person person = new Person();
            while (rs.next()) {
                person.setId(rs.getLong(1));
                person.setName(rs.getString(2));
                person.setLastName(rs.getString(3));
                person.setMiddleName(rs.getString(4));
            }
            rs.close();

            return person;
        }

    }

    public List<Person> findAll() throws SQLException {
        String selectQuery = "SELECT * FROM person;";
        try(Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(selectQuery);
            if (!rs.isBeforeFirst()) {
                return null;
            }
            ArrayList<Person> persons = new ArrayList<>();
            while (rs.next()) {
                persons.add(new Person(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                ));
            }
            rs.close();
            return persons;
        }
    }

}
