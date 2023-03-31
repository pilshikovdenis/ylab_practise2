package com.pilshikov.io.ylab.intensive.lesson04.movie;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MovieLoaderImpl implements MovieLoader {
  private DataSource dataSource;

  public MovieLoaderImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void loadData(File file) {
    // РЕАЛИЗАЦИЮ ПИШЕМ ТУТ
    try {
      List<Movie> movieList = getMoviesListFromFile(file);

      for (Movie movie : movieList) {
        saveMovie(movie, dataSource);
      }
    } catch (IOException | SQLException e) {
      System.out.println(e.getMessage());
    }

  }

  // Метод получает на вход строку, разрезает ее и парсит ее и возвращает Movie
  // Для всех полей где строка является пустой, устанавливается null
  private static Movie parseMovieFromString(String data) {
    if (data.isEmpty()) {
      return null;
    }

    String[] parsedFields = data.split(";");
    Movie movie = new Movie();
    movie.setYear(
            parsedFields[0].isEmpty() ? null : Integer.valueOf(parsedFields[0])
    );
    movie.setLength(
            parsedFields[1].isEmpty() ? null : Integer.valueOf(parsedFields[1])
    );
    movie.setTitle(
            parsedFields[2].isEmpty() ? null : parsedFields[2]
    );
    movie.setSubject(
            parsedFields[3].isEmpty() ? null : parsedFields[3]
    );
    movie.setActors(
            parsedFields[4].isEmpty() ? null : parsedFields[4]
    );
    movie.setActress(
            parsedFields[5].isEmpty() ? null : parsedFields[5]
    );
    movie.setDirector(
            parsedFields[6].isEmpty() ? null : parsedFields[6]
    );

    movie.setPopularity(
            parsedFields[7].isEmpty() ? null : Integer.valueOf(parsedFields[7])
    );
    movie.setAwards(
            parsedFields[8].isEmpty() ? null : parsedFields[8].equals("Yes")
    );

    return movie;
  }

  //Метод получает на вход файл, парсит его и  возвращает список со всеми Movie
  private static List<Movie> getMoviesListFromFile(File file) throws IOException {
    List<Movie> movieList = new ArrayList<>();

    try (FileInputStream inputStream = new FileInputStream(file)) {
      Scanner scanner = new Scanner(inputStream);
      // пропуск первых двух строк
      scanner.nextLine();
      scanner.nextLine();

      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        movieList.add(parseMovieFromString(line));
      }
    }

    return movieList;
  }

  //Метод добавляет Movie в базу данных
  private static void saveMovie(Movie movie, DataSource dataSource) throws SQLException {
    String insertQuery = "INSERT INTO movie\n" +
            "(\"year\", length, title, subject, actors, actress, director, popularity, awards)\n" +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);\n";
    try(Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

      prepareIntegerParam(preparedStatement, 1, movie.getYear());
      prepareIntegerParam(preparedStatement, 2, movie.getLength());
      prepareStringParam(preparedStatement, 3, movie.getTitle());
      prepareStringParam(preparedStatement, 4, movie.getSubject());
      prepareStringParam(preparedStatement, 5, movie.getActors());
      prepareStringParam(preparedStatement, 6, movie.getActress());
      prepareStringParam(preparedStatement, 7, movie.getDirector());
      prepareIntegerParam(preparedStatement, 8, movie.getPopularity());

      if (movie.getAwards() == null) {
        preparedStatement.setNull(9, Types.NULL);
      } else {
        preparedStatement.setBoolean(9, movie.getAwards());
      }

      preparedStatement.executeUpdate();
    }
  }

  // Метод устанавливает в PreparedStatement значение value с типом String, для параметра c номером index
  private static void prepareStringParam(PreparedStatement preparedStatement, int index, String value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(index, Types.NULL);
    }
    else {
      preparedStatement.setString(index, value);
    }
  }
  // Метод устанавливает в PreparedStatement значение value с типом Ineger, для параметра c номером index
  private static void prepareIntegerParam(PreparedStatement preparedStatement, int index, Integer value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(index, Types.NULL);
    }
    else {
      preparedStatement.setInt(index, value);
    }
  }
}
