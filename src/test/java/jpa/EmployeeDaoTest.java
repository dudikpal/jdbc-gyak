package jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDaoTest {

    private EmployeeDao employeeDao;

    @BeforeEach
    void init() {
        /*MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3308/employees");
        dataSource.setUser("employees");
        dataSource.setPassword("employees");

        Flyway flyway = Flyway.configure()
                .locations("filesystem:src/test/resources/jpa")
                .dataSource(dataSource)
                .load();
        flyway.clean();
        flyway.migrate();*/

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("pu");
        employeeDao = new EmployeeDao(entityManagerFactory);


    }

    @Test
    void testSaveThenById() {
        Employee employee = new Employee("John Doe");
        employeeDao.save(employee);

        long id = employee.getId();

        Employee another = employeeDao.finById(id);
        assertEquals("John Doe", another.getName());
    }

    @Test
    void testSaveThanListAll() {
        employeeDao.save(new Employee("John Doe"));
        employeeDao.save(new Employee( "Jane Doe"));
        /*employeeDao.save(new Employee("John Doe"));
        employeeDao.save(new Employee("Jane Doe"));*/

        List<Employee> employees = employeeDao.listAll();
        List<String> names = employees.stream().map(Employee::getName).collect(Collectors.toList());
        assertEquals(List.of("Jane Doe", "John Doe"), names);
    }

    @Test
    void testNameChange() {
        Employee employee = new Employee("John Doe");
        employeeDao.save(employee);

        long id = employee.getId();

        employeeDao.changeName(id, "Jack Doe");

        Employee another = employeeDao.finById(id);
        assertEquals("Jack Doe", another.getName());
    }

    @Test
    void testDelete() {
        Employee employee = new Employee("John Doe");
        employeeDao.save(employee);
        long id = employee.getId();
        employeeDao.delete(1L);

        List<Employee> employees = employeeDao.listAll();
        assertTrue(employees.isEmpty());
    }

    @Test
    void testEmployeeWithAttributes() {

        for (int i = 0; i < 10; i++) {
            employeeDao.save(new Employee("John Doe", Employee.EmployeeType.HALF_TIME,
                    LocalDate.of(2000, 1, 1)));
        }
        Employee employee = employeeDao.listAll().get(0);
        System.out.println(employee);
        assertEquals(LocalDate.of(2000, 1, 1), employee.getDateOfBirth());
    }


    @Test
    void testNickName() {
        Employee employee = new Employee("John Doe");
        employee.setNickNames(Set.of("Johnny", "Jíí"));
        employeeDao.save(employee);

        Employee anotherEmployee = employeeDao.findEmployeeByIdWithNicknames(employee.getId());
        System.out.println(anotherEmployee.getNickNames());
        assertEquals(Set.of("Johnny", "Jíí"), anotherEmployee.getNickNames());
    }

    @Test
    void testVacations() {
        Employee employee = new Employee("John Doe");
        employee.setVacationBookings(Set.of(new VacationEntry(LocalDate.of(2018, 1, 1), 4),
                new VacationEntry(LocalDate.of(2018, 2, 15), 2)));
        employeeDao.save(employee);

        Employee anotherEmployee = employeeDao.findEmployeeByIdWithVacations(employee.getId());
        assertEquals(2, anotherEmployee.getVacationBookings().size());
    }

    /*@Test
    void testPhoneNumbers() {
        Employee employee = new Employee("John Doe");
        employee.setPhoneNumbers(Map.of("home", "1234", "work", "4321"));
        employeeDao.save(employee);
        Employee anotherEmployee = employeeDao.findEmployeeByIdWithPhoneNumbers(employee.getId());
        assertEquals("1234", anotherEmployee.getPhoneNumbers().get("home"));
        assertEquals("4321", anotherEmployee.getPhoneNumbers().get("work"));
    }*/

    @Test
    void testPhoneNumber() {
        PhoneNumber phoneNumberWork = new PhoneNumber("work", "4321");
        PhoneNumber phoneNumberHome = new PhoneNumber("home", "1234");

        Employee employee = new Employee("John Doe");
        //employee.setPhoneNumbers(Set.of(phoneNumberHome, phoneNumberWork));
        employee.addPhoneNumber(phoneNumberWork);
        employee.addPhoneNumber(phoneNumberHome);
        employeeDao.save(employee);

        Employee anotherEmployee = employeeDao.findEmployeeByIdWithPhoneNumbers(employee.getId());
        //System.out.println(anotherEmployee.getPhoneNumbers());
        assertEquals(2, anotherEmployee.getPhoneNumbers().size());
        assertEquals("work", anotherEmployee.getPhoneNumbers().get(0).getType());
    }

    @Test
    void testAddPhoneNumber() {
        Employee employee = new Employee("John Doe");
        employeeDao.save(employee);

        employeeDao.addPhoneNumber(employee.getId(), new PhoneNumber("homm", "1111"));

        Employee anotherEmployee = employeeDao.findEmployeeByIdWithPhoneNumbers(employee.getId());
        assertEquals(1, anotherEmployee.getPhoneNumbers().size());
    }

    @Test

    void testRemove() {
        Employee employee = new Employee("John Doe");
        employee.addPhoneNumber(new PhoneNumber("home", "1111"));
        employee.addPhoneNumber(new PhoneNumber("wokr", "2222"));
        employeeDao.save(employee);

        employeeDao.delete(employee.getId());
    }

}