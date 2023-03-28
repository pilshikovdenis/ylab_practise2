package com.pilshikov.io.ylab.intensive.lesson04.filesort;

import javax.sql.DataSource;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileSortImpl implements FileSorter {
  private DataSource dataSource;

  public FileSortImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public File sort(File data) {
    // ТУТ ПИШЕМ РЕАЛИЗАЦИЮ
    String resultFilePath = "final.txt";

    try {
      ArrayList<Long> numbers = parseNumbersFromFile(data);
      if (numbers == null) {
        return null;
      }

//      // тест скорости обычной вставки
//      System.out.println("INSERT START AT" + new Date());
//      insertData(dataSource, numbers);
//      System.out.println("INSERT END AT " + new Date());
//      clearTable(dataSource);
      // по итогу оказалось 10 секунд против 10 минут для 1млн чисел

      System.out.println("BATCH INSERT START AT" + new Date());
      batchInsertData(dataSource, numbers);
      System.out.println("BATCH INSERT END AT " + new Date());

      // получаем отсортированный список с числами из бд
      ArrayList<Long> sortedNumbers = getSortedNumbersFromDb(dataSource);
      // записываем их
      writeArrayToFile(resultFilePath, sortedNumbers);

    } catch (IOException|SQLException e) {
      throw new RuntimeException(e);
    }

    return new File(resultFilePath);
  }

  // Метод возвращает все числа из файла в виде массива
  private ArrayList<Long> parseNumbersFromFile(File data) throws IOException {
    // считываем сразу весь файл как массив байтов
    byte[] bytes = new byte[(int) data.length()];
    FileInputStream fis = new FileInputStream(data);
    fis.read(bytes);
    fis.close();

    // получаем строку из массива байт и разрезаем
    String[] parsedStrings = new String(bytes).split("\\s+");
    if (parsedStrings.length == 0) {
      return null;
    }

    // заполняем массив и возвращаем
    ArrayList<Long> numbers = new ArrayList<>();
    for (String parsedString : parsedStrings) {
      numbers.add(Long.parseLong(parsedString));
    }

    return numbers;
  }

  // Метод принимает массив чисел и добавляет их в бд, вставка обычная
  private static void insertData(DataSource dataSource, List<Long> numbers) throws SQLException {
    String insertQuery = "INSERT INTO numbers (val) VALUES(?);\n";

    try(Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
      for (Long number : numbers) {
        preparedStatement.setLong(1, number);
        preparedStatement.executeUpdate();
      }
    }
  }

  // Метод принимает массив чисел и добавляет их в бд, batch вставка
  private static void batchInsertData(DataSource dataSource, List<Long> numbers) throws SQLException {
    String insertQuery = "INSERT INTO numbers (val) VALUES(?);";

    try(Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
      for (Long number : numbers) {
        preparedStatement.setLong(1, number);
        preparedStatement.addBatch();
      }
      preparedStatement.executeBatch();
    }
  }

  // Метод возвращает массив с числами отсортированными в порядке убывания
  private static ArrayList<Long> getSortedNumbersFromDb(DataSource dataSource) throws SQLException {
    String selectQuery = "select * from numbers order by val desc ;";
    try(Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(selectQuery);

      ArrayList<Long> results = new ArrayList<>();
      while (rs.next()) {
        results.add(rs.getLong(1));
      }

      return (results.isEmpty()) ? null : results;
    }
  }

  // Метод записывает массив в файл
  private void writeArrayToFile(String filePath, ArrayList<Long> data) throws FileNotFoundException {
    try(PrintWriter pw = new PrintWriter(filePath)) {
      for (long e : data) {
        pw.println(e);
      }
      pw.flush();
    }
  }

  // Для дебага
  // Метод очищает таблицу с числами
  private void clearTable(DataSource dataSource) throws SQLException {
    try (Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement()) {
      statement.execute("DELETE FROM numbers ;");
    }
  }
}
