package employees;


import org.flywaydb.core.Flyway;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeesDaoTest {

    static EmployeesDao employeesDao;
    static  MariaDbDataSource ds;

    @BeforeAll
    static void init() throws SQLException{

        ds = new MariaDbDataSource();
        ds.setUrl("jdbc:mariadb://localhost:3308/employees?useUnicode=true");
        ds.setUser("employees");
        ds.setPassword("employees");
        employeesDao = new EmployeesDao(ds);
    }
    @BeforeEach
    public void setUp() throws SQLException{
        employeesDao = new EmployeesDao(ds);
        Flyway flyway = Flyway
                .configure()
                .locations("filesystem:src/test/resources/employees/db")
                .dataSource(ds)
                .load();
        flyway.clean();
        flyway.migrate();
    }


    @Test
    public void testInsert() {
        employeesDao.createEmployee("vacak");
        employeesDao.createEmployee("vacak2");
        employeesDao.createEmployee("vacak3");
        assertEquals(List.of("vacak", "vacak2", "vacak3"), employeesDao.listEmployeeNames());
    }

    @Test
    void testById() {
        long id = employeesDao.createEmployee("vaczak");
        System.out.println(id);
        id = employeesDao.createEmployee("vaczak2");
        System.out.println(id);
        String name = employeesDao.findEmployeeNameById(id);
        assertEquals("vaczak2", name);
    }

    @Test
    void testCreateEmployees() {
        List<String> inputNames = List.of("első", "mashodik", "harmadikh");
        employeesDao.createEmployees(inputNames);
        List<String> names = employeesDao.listEmployeeNames();
        assertEquals(inputNames, names);
    }

    @Test
    void testCreateEmployeesRollback() {
        List<String> inputNames = List.of("első", "mashodik", "xharmadikh");
        employeesDao.createEmployees(inputNames);
        List<String> names = employeesDao.listEmployeeNames();
        assertEquals(Collections.emptyList(), names);
    }

    @Test
    void testOddNames() {
        employeesDao.createEmployees(List.of("Jack Doe", "Jane Doe", "John Doe"));
        List<String> names = employeesDao.listOddEmployeeNames();
        List<String> expected = List.of("Jack Doe", "John Doe");
        assertEquals(expected, names);
    }

    @Test
    void testUpdateNames() {
        employeesDao.createEmployees(List.of("Jack Doe", "Jane Doe", "Joe Doe"));
        employeesDao.updateNames();
        List<String> names = employeesDao.listEmployeeNames();
        assertEquals(List.of("Mr. Jack Doe", "Jane Doe", "Mr. Joe Doe"), names);
    }

}
