package com.pilshikov.homework3.dated_map;



public class DatedMapTest {
    public static void main(String[] args) {
        DatedMap datedMap = new DatedMapImpl();
        datedMap.put("first", "value1");
        delay(1000);
        datedMap.put("second", "value2");
        delay(1000);
        datedMap.put("third", "value3");
        delay(1000);
        datedMap.put("four", "value4");

        // выводим все данные
        for(String key  : datedMap.keySet()) {
            System.out.println("K: " + key + " V: " + datedMap.get(key) + " D: " + datedMap.getKeyLastInsertionDate(key));
        }
        System.out.println();


        // проверяем что хранится дата последнего добавления
        delay(2000);
        datedMap.put("four", "value4");
        System.out.println(datedMap.getKeyLastInsertionDate("four"));


        // пробуем получить данные по несуществующему ключу
        System.out.println(datedMap.get("five"));
        System.out.println(datedMap.getKeyLastInsertionDate("five"));


        System.out.println(datedMap.containsKey("five"));


        // проверяем удаление
        datedMap.remove("first");
        datedMap.remove("second");
        for(String key  : datedMap.keySet()) {
            System.out.println("K: " + key + " V: " + datedMap.get(key) + " D: " + datedMap.getKeyLastInsertionDate(key));
        }
    }

    public static void delay(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
