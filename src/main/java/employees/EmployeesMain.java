package employees;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class EmployeesMain {

    public static void main(String[] args) {
        MariaDbDataSource ds = new MariaDbDataSource();
        try {
            ds.setUrl("jdbc:mariadb://localhost:3308/employees?useUnicode=true");
            ds.setUser("employees");
            ds.setPassword("employees");
        } catch (SQLException se) {
            throw new IllegalStateException("cannot db");
        }


        EmployeesDao employeesDao = new EmployeesDao(ds);
        employeesDao.createEmployee("Jack Doe");
        List<String> names = employeesDao.listEmployeeNames();
        System.out.println(names);
        String name = employeesDao.findEmployeeNameById(1L);
        System.out.println(name);
    }
}
