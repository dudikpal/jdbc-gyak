package jpa;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.flywaydb.core.Flyway;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class EmployeeMain {

    public static void main(String[] args) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3308/employees");
        dataSource.setUser("employees");
        dataSource.setPassword("employees");

        Flyway flyway = Flyway.configure()
                .locations("filesystem:src/test/resources/jpa")
                .dataSource(dataSource)
                .load();
        flyway.clean();
        flyway.migrate();

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("pu");
        EntityManager em = factory.createEntityManager();
        Employee employee;
        em.getTransaction().begin();
        for (int i = 0; i < 14; i++) {
            employee = new Employee();
            employee.setName("Doe " + i);
            em.persist(employee);
        }
        em.getTransaction().commit();

        Employee saved = em.find(Employee.class, 7L);
        System.out.println(saved.getId() + " " + saved.getName());

        List<Employee> employees = em.createQuery("select e from Employee e order by e.name",
                Employee.class).getResultList();

        System.out.println(employees);

        em.getTransaction().begin();
        Employee employeeToModify = em.find(Employee.class, employees.get(4).getId());
        System.out.println(employeeToModify.getName());
        employeeToModify.setName("Javított");
        em.getTransaction().commit();

        em.getTransaction().begin();
        Employee employeeToDelete = em.find(Employee.class, employees.get(4).getId());
        em.remove(employeeToDelete);
        em.getTransaction().commit();

        List<Employee> newEmployees = em.createQuery("select e from Employee e",
                Employee.class).getResultList();
        System.out.println(newEmployees);

        em.close();
        factory.close();
    }
}
