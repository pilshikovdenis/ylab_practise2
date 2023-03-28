package com.pilshikov.io.ylab.intensive.lesson04.persistentmap;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, методы которого надо реализовать 
 */
public class PersistentMapImpl implements PersistentMap {
  private String name;
  private DataSource dataSource;

  public PersistentMapImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void init(String name) {
    this.name = name;
  }

  @Override
  public boolean containsKey(String key) throws SQLException {
    String selectQuery = "SELECT COUNT(*) FROM persistent_map WHERE map_name = ? AND key = ?;";
    try(Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
      preparedStatement.setString(1, this.name);
      preparedStatement.setString(2, key);

      ResultSet rs = preparedStatement.executeQuery();

      rs.next();
      return rs.getInt(1) != 0;
    }
  }

  @Override
  public List<String> getKeys() throws SQLException {
    String selectQuery = "SELECT key FROM persistent_map WHERE map_name = ?;";
    try(Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
      preparedStatement.setString(1, this.name);

      ResultSet rs = preparedStatement.executeQuery();
      List<String> keys = new ArrayList<>();
      while (rs.next()) {
        keys.add(rs.getString(1));
      }

      return (keys.size() == 0) ? null : keys;
    }

  }

  @Override
  public String get(String key) throws SQLException {
    String selectQuery = "SELECT value FROM persistent_map WHERE map_name = ? AND key = ?;";
    try(Connection connection = dataSource.getConnection();
    PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
      preparedStatement.setString(1, this.name);
      preparedStatement.setString(2, key);


      ResultSet rs = preparedStatement.executeQuery();

      if (rs.next()) {
        return rs.getString(1);
      } else {
        return null;
      }

    }
  }

  @Override
  public void remove(String key) throws SQLException {
    String deleteQuery = "DELETE FROM persistent_map WHERE map_name = ? AND key = ?;";
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
      preparedStatement.setString(1, this.name);
      preparedStatement.setString(2, key);
      preparedStatement.execute();
    }
  }

  @Override
  public void put(String key, String value) throws SQLException {
    String deleteQuery = "DELETE FROM persistent_map WHERE map_name = ? AND key  = ?;";
    String insertQuery = "INSERT INTO persistent_map (map_name, key, value) VALUES(?, ?, ?);";

    try(Connection connection = dataSource.getConnection();
        PreparedStatement deletePrepareStatement = connection.prepareStatement(deleteQuery);
        PreparedStatement insertPrepareStatement = connection.prepareStatement(insertQuery)) {
        // удаление
      deletePrepareStatement.setString(1, this.name);
      deletePrepareStatement.setString(2, key);
      deletePrepareStatement.execute();

      // добавление
      insertPrepareStatement.setString(1, this.name);
      insertPrepareStatement.setString(2, key);
      insertPrepareStatement.setString(3, value);
      insertPrepareStatement.executeUpdate();


    }
  }

  @Override
  public void clear() throws SQLException {
    String deleteQuery = "DELETE FROM persistent_map WHERE map_name = ?;";
    try(Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
      preparedStatement.setString(1, this.name);
      preparedStatement.execute();
    }
  }
}
