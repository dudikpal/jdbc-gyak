package jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

class ParkingPlaceDaoTest {

    private ParkingPlaceDao parkingPlaceDao;
    private EmployeeDao employeeDao;

    @BeforeEach
    void init() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("pu");
        parkingPlaceDao = new ParkingPlaceDao(factory);
        employeeDao = new EmployeeDao(factory);
    }


    @Test
    void testSave() {
        parkingPlaceDao.saveParkingPlace(new ParkingPlace(100));
        ParkingPlace parkingPlace = parkingPlaceDao.findParkingPlacenbumber(100);
        assertEquals(100, parkingPlace.getNumber());
    }

    // futtatés előtt el kell dobni a táblákat!!!
    @Test
    void testSaveEmployeeWithParkingPlace() {
        ParkingPlace parkingPlace = new ParkingPlace(200);
        parkingPlaceDao.saveParkingPlace(parkingPlace);
        Employee employee = new Employee("John Doe");
        employee.setParkingPlace(parkingPlace);
        employeeDao.save(employee);

        Employee anotherEmployee = employeeDao.finById(employee.getId());
        assertEquals(200, anotherEmployee.getParkingPlace().getNumber());
    }

}