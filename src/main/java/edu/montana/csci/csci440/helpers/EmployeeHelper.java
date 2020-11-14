package edu.montana.csci.csci440.helpers;

import edu.montana.csci.csci440.model.Employee;

import java.util.*;

public class EmployeeHelper {
    public static String makeEmployeeTree() {
        Employee employee = Employee.find(1); // root employee
        // and use this data structure to maintain reference information needed to build the tree structure
        Map<Long, List<Employee>> employeeMap = new HashMap<>();

        for (Employee emp: Employee.all()) {
            Long bossId = emp.getReportsTo();
            List<Employee> employees = employeeMap.get(bossId);
            if (employees == null) {
                employees = new LinkedList<>();
                employeeMap.put(bossId, employees);
            }
            employees.add(emp);


        }

        return "<ul>" + makeTree(employee, employeeMap) + "</ul>";
    }

    public static String makeTree(Employee employee, Map<Long, List<Employee>> employeeMap) {
        String list = "<li><a href='/employees" + employee.getEmployeeId() + "'>"
                + employee.getEmail() + "</a><ul>";
        //List<Employee> reports = new LinkedList<>();
        List<Employee> reports = employeeMap.get(employee.getEmployeeId());
        if (reports == null) {
            return list + "</ul></li>";
        } else {
            for (Employee report : reports) {
                list += makeTree(report, employeeMap);
            }
        }

        return list + "</ul></li>";
    }
}
