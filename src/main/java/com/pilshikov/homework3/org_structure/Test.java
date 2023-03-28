package com.pilshikov.homework3.org_structure;

import java.io.File;
import java.io.IOException;


public class Test {
    public static void main(String[] args) {
        // для удобного тестирования не были удалены Employee.ToString() и OrgStructureParserImpl.printEmployees()

        OrgStructureParser parser = new OrgStructureParserImpl();
        File file = new File("src/homework3/org_structure/data.csv");
        try {
            Employee boss = parser.parseStructure(file);
            System.out.println(boss);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
