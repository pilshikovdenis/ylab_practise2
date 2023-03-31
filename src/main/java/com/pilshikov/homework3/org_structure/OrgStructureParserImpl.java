package com.pilshikov.homework3.org_structure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class OrgStructureParserImpl implements OrgStructureParser {


    @Override
    public Employee parseStructure(File csvFile) throws IOException {
        // коллекция для хранения Employee
        Map<Long, Employee> employeeMap = new HashMap<>();


        try (FileInputStream inputStream = new FileInputStream(csvFile)){
            Scanner scanner = new Scanner(inputStream);
            // пропускаем первую строку
            scanner.nextLine();

            while (scanner.hasNextLine()) {
                // парсим строку, создаем объект и сохраняем его
                String data = scanner.nextLine();

                Employee newEmployee = parseEmployeeFromString(data);
                if (newEmployee != null) {
                    employeeMap.put(newEmployee.getId(), newEmployee);
                }

            }
        } catch (IOException e) {
            return null;
        } catch (IncorrectStringToParseException e) {
            // если найдется хотя бы одна некорректная строка, кидаем исключение
            System.out.println(e.getMessage());
            return null;
        }

        buildRelationships(employeeMap);

        // for debug only
        printEmployees(employeeMap);

        return findBoss(employeeMap);
    }

    public Employee parseEmployeeFromString(String data) throws IncorrectStringToParseException {
        // разрезаем строку
        String[] parsedFields = data.split(";");
        // даже если boss_id не будет задан, все равно корректная строка будет содержать 4 поля
        // в противном случае
        if (parsedFields.length != 4) {
            throw new IncorrectStringToParseException("Невозможно прочитать все поля из строки " + data);
        }
        Employee employee = new Employee();
        employee.setId(Long.valueOf(parsedFields[0]));
        employee.setName(parsedFields[2]);
        employee.setPosition(parsedFields[3]);

        // если получен BossId то добавляем его объекту
        if (! parsedFields[1].isEmpty()) {
            employee.setBossId(Long.valueOf(parsedFields[1]));
        }

        return employee;
    }

    public void buildRelationships(Map<Long, Employee> map) {
        // добавляем боссу подчиненного, и наоборот
        for (Employee employee : map.values()) {
            if (employee.getBossId() != null) {
                Employee boss = map.get(employee.getBossId());
                employee.setBoss(boss);
                boss.getSubordinate().add(employee);
            }
        }
    }

    public Employee findBoss(Map<Long, Employee> employeeMap) {
        // ищем Employee у которого не установлен BossId
        for (Employee employee : employeeMap.values()) {
            if (employee.getBossId() == null)
                return employee;
        }
        return null;
    }


    // for debug only
    public void printEmployees(Map<Long, Employee> map) {
        for (Map.Entry<Long, Employee> set : map.entrySet()) {
            Employee employee = set.getValue();
            System.out.println("Id " + employee.getId());
            System.out.println("Boss id " + employee.getBossId());
            System.out.println("Boss " + employee.getBoss());
            System.out.println("Name " + employee.getName());
            System.out.println("Position " + employee.getPosition());
            System.out.println("Subbordinate " + employee.getSubordinate());
            System.out.println();
        }
    }


}
