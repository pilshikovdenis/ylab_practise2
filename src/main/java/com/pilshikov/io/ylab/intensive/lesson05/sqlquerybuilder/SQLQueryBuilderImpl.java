package com.pilshikov.io.ylab.intensive.lesson05.sqlquerybuilder;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class SQLQueryBuilderImpl implements SQLQueryBuilder {

    private DataSource dataSource;


    public SQLQueryBuilderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Метод возвращает select запрос для таблицы со всеми столбцами
    @Override
    public String queryForTable(String tableName) throws SQLException {
        if (!getTables().contains(tableName)) {
            return null;
        }
        else {
            List<String> columns = getColumnTitles(tableName);
            String columnTitlesWithDelimeter = String.join(",", columns);
            return "SELECT " + columnTitlesWithDelimeter + " FROM " + tableName;
        }
    }

    // Метод возвращает список всех таблиц базы данных, null в случае если их там нет
    @Override
    public List<String> getTables() throws SQLException {
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

    // Метод возвращает список с названием столбцов таблицы
    private List<String> getColumnTitles(String tableName) throws SQLException {
        String selectQuery = "SELECT * FROM " + tableName + " LIMIT 1;";

        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery(selectQuery);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            List<String> titles = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                titles.add(rsmd.getColumnName(i));
            }

            return titles;
        }
    }

}
