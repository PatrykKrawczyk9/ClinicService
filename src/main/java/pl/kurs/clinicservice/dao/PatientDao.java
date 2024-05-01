package pl.kurs.clinicservice.dao;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.kurs.clinicservice.models.Patient;

import java.util.List;

@Repository
public class PatientDao implements IPatientDao {

    @Autowired
    private EntityManagerFactory factory;

    @Override
    public Patient save(Patient patient) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        entityManager.persist(patient);
        tx.commit();
        entityManager.close();
        return patient;
    }

    @Override
    public List<Patient> saveAll(List<Patient> patients) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        patients.forEach(entityManager::persist);
        tx.commit();
        entityManager.close();
        return patients;
    }

    @Override
    public Patient findById(Long patientId) {
        EntityManager entityManager = factory.createEntityManager();
        Patient patient = entityManager.find(Patient.class, patientId);
        entityManager.close();
        return patient;
    }

    @Override
    public Patient findByIdWithVisits(Long patientId) {
        try (EntityManager entityManager = factory.createEntityManager()) {

            String jpqlQuery = "SELECT p FROM Patient p JOIN FETCH p.visits v JOIN FETCH v.doctor d WHERE p.patientId = :patientId";
            TypedQuery<Patient> query = entityManager.createQuery(jpqlQuery, Patient.class);
            query.setParameter("patientId", patientId);

            return query.getResultStream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Pacjent o id: " + patientId + " nie zostal znaleziony"));
        }
    }
}
