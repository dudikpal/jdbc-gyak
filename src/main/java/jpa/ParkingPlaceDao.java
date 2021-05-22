package jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ParkingPlaceDao {

    private EntityManagerFactory factory;

    public ParkingPlaceDao(EntityManagerFactory factory) {
        this.factory = factory;
    }

    public void saveParkingPlace(ParkingPlace parkingPlace) {
        EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(parkingPlace);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public ParkingPlace  findParkingPlacenbumber(int number) {
        EntityManager entityManager = factory.createEntityManager();
        ParkingPlace parkingPlace = entityManager
                .createQuery("select p from ParkingPlace p where p.number = :number",
                        ParkingPlace.class)
                .setParameter("number", number)
                .getSingleResult();
        entityManager.close();
        return parkingPlace;
    }
}
