package jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class EmployeeDao {

    private EntityManagerFactory entityManagerFactory;

    public EmployeeDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void save(Employee employee) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(employee);
        em.getTransaction().commit();
        em.close();
    }

    public Employee finById(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Employee employee = em.find(Employee.class, id);
        //System.out.println(employee.getNickNames());
        em.close();
        return employee;
    }

    public Employee findEmployeeByIdWithNicknames(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Employee employee = entityManager
                .createQuery("select e from Employee e join fetch e.nickNames where id = :id",
                        Employee.class)
                .setParameter("id", id)
                .getSingleResult();
        entityManager.close();
        return employee;
    }

    public List<Employee> listAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        List<Employee> employees = em.createQuery("select e from Employee e order by e.name", Employee.class)
                .getResultList();
        em.close();
        return employees;
    }

    public void changeName(Long id, String name) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Employee employee = em.find(Employee.class, id);
        employee.setName(name);
        em.getTransaction().commit();
        em.close();
    }

    public void delete(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Employee employee = em.getReference(Employee.class, id);
        em.remove(employee);
        em.getTransaction().commit();
        em.close();
    }

    public Employee findEmployeeByIdWithVacations(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Employee employee = entityManager
                .createQuery("select e from Employee e join fetch e.vacationBookings where id = :id",
                        Employee.class)
                .setParameter("id", id)
                .getSingleResult();
        entityManager.close();
        return employee;
    }


    public void addPhoneNumber(Long id, PhoneNumber phoneNumber) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        //Employee employee = entityManager.find(Employee.class, id);
        Employee employee = entityManager.getReference(Employee.class, id);
        phoneNumber.setEmployee(employee);
        entityManager.persist(phoneNumber);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public Employee findEmployeeByIdWithPhoneNumbers(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Employee employee = entityManager
                .createQuery("select e from Employee e join fetch e.phoneNumbers where e.id = :id",
                        Employee.class)
                .setParameter("id", id)
                .getSingleResult();
        entityManager.close();
        return employee;
    }

    /*public void findEmployeeByIdWithPhoneNumbers(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Employee employee = entityManager
                .createQuery("select e from Employee e join fetch e.phoneNumbers  where id = :id",
                Employee.class)
                .setParameter("id", id)
                .getSingleResult();
        entityManager.close();
    }*/
}
