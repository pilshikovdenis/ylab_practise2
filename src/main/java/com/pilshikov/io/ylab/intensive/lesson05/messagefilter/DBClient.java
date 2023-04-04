package com.pilshikov.io.ylab.intensive.lesson05.messagefilter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class DBClient {
    private DataSource dataSource;
    private BlackListUtility blackListUtility;

    public DBClient(DataSource dataSource,BlackListUtility blackListUtility) {
        this.dataSource = dataSource;
        this.blackListUtility = blackListUtility;
    }

    @PostConstruct
    private void cleanAndUpdateBlacklist() throws SQLException, IOException {
        // Метод создает таблицу если ее нет, если есть очищает
        // Далее загружает новый список запрещенных слов в базу
        List<String> tables = getTables();

        if (tables.contains("words")) {
            System.out.println("Очистка таблицы words");
            cleanTable();

        } else {
            System.out.println("Создание таблицы words");
            createTable();
        }

        System.out.println("Загрузка черного списка");
        uploadBlackList(blackListUtility.getBlackList());

    }

    // Метод возвращает список таблиц в базе данных
    private  List<String> getTables() throws SQLException {
        List<String> results = new ArrayList<>();

        try(Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            String[] types = {"TABLE"};
            ResultSet tables = metaData.getTables(null, null, "%", types);
            while (tables.next()) {
                results.add(tables.getString("TABLE_NAME"));
            }
            tables.close();
        }
        return (results.isEmpty()) ? null : results;
    }

    // Метод создает таблицу words в бд
    private void createTable() throws SQLException {
        String ddl = "create table words (" +
                "word varchar not null," +
                "constraint word_pkey primary key (word)" +
                ");";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(ddl);
        }
    }

    // Метод удаляет все записи в таблице
    private void cleanTable() throws SQLException {
        String ddl = "DELETE FROM words" ;
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            statement.execute(ddl);
        }
    }

    // Метод получает список со словами и загружает его в таблицу
    private void uploadBlackList(List<String> blackList) throws SQLException, IOException {
        String insertQuery = "INSERT INTO words (word) VALUES(?);";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            for (String word : blackList) {
                preparedStatement.setString(1, word);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }

    }

    // Метод проверяет находится ли полученное слово в базе данных
    public boolean isWordValid(String word) throws SQLException {

        String query = "SELECT * FROM words WHERE word = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, word.toLowerCase());

            ResultSet rs = preparedStatement.executeQuery();
            return ! rs.isBeforeFirst();
        }
    }
}
