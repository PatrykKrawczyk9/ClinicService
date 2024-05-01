package pl.kurs.clinicservice.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.kurs.clinicservice.models.Visit;

import java.util.List;

@Repository
public class VisitDao implements IVisitDao{

    @Autowired
    private EntityManagerFactory factory;


    @Override
    public Visit save(Visit visit) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        entityManager.persist(visit);
        tx.commit();
        entityManager.close();
        return visit;
    }

    @Override
    public List<Visit> saveAll(List<Visit> visits) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        visits.forEach(entityManager::persist);
        tx.commit();
        entityManager.close();
        return visits;
    }

    @Override
    public Visit findById(Long visitId) {
        EntityManager entityManager = factory.createEntityManager();
        Visit visit = entityManager.find(Visit.class, visitId);
        entityManager.close();
        return visit;
    }
}
