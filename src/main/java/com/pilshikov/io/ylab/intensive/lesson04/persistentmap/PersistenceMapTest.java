package com.pilshikov.io.ylab.intensive.lesson04.persistentmap;



import com.pilshikov.io.ylab.intensive.lesson04.DbUtil;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class PersistenceMapTest {
  public static void main(String[] args) throws SQLException {
    DataSource dataSource = initDb();
    PersistentMap persistentMap = new PersistentMapImpl(dataSource);
    // Написать код демонстрации работы

    persistentMap.init("map1");

    persistentMap.put("key1", "value1");
    persistentMap.put("key1", "value12");
    persistentMap.put("age", "20");
    persistentMap.put("key3", "value3");

    String val3 = persistentMap.get("key3");
    System.out.println(val3);

    System.out.println(persistentMap.containsKey("key7"));

    List<String> keysOfMap1 = persistentMap.getKeys();
    System.out.println(keysOfMap1);
    persistentMap.remove("age");
    System.out.println(persistentMap.getKeys());

    persistentMap.clear();
    System.out.println(persistentMap.get("key1"));
    System.out.println(persistentMap.getKeys());
    System.out.println();


    persistentMap.init("map2");
    persistentMap.put("name", "Nick");
    persistentMap.put("surname", "Peterson");
    persistentMap.put("age", "22");
    for (String key : persistentMap.getKeys()) {
      System.out.printf("K: %s V: %s \n", key, persistentMap.get(key));
    }



  }
  
  public static DataSource initDb() throws SQLException {
    String createMapTable = "" 
                                + "drop table if exists persistent_map; " 
                                + "CREATE TABLE if not exists persistent_map (\n"
                                + "   map_name varchar,\n"
                                + "   KEY varchar,\n"
                                + "   value varchar\n"
                                + ");";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(createMapTable, dataSource);
    return dataSource;
  }
}
