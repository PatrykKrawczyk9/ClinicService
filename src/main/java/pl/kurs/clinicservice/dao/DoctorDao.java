package pl.kurs.clinicservice.dao;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.kurs.clinicservice.models.Doctor;
import pl.kurs.clinicservice.models.Visit;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DoctorDao implements IDoctorDao {

    @Autowired
    private EntityManagerFactory factory;

    @Override
    public Doctor save(Doctor doctor) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        entityManager.persist(doctor);
        tx.commit();
        entityManager.close();
        return doctor;
    }

    @Override
    public List<Doctor> saveAll(List<Doctor> doctors) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        doctors.forEach(entityManager::persist);
        tx.commit();
        entityManager.close();
        return doctors;
    }

    @Override
    public Doctor findById(Long doctorId) {
        EntityManager entityManager = factory.createEntityManager();
        Doctor doctor = entityManager.find(Doctor.class, doctorId);
        entityManager.close();
        return doctor;
    }

    @Override
    public LocalDate findFirstAvailableVisitAfterDate(Long doctorId, LocalDate startDate) {
        try (EntityManager entityManager = factory.createEntityManager()) {

            String jpqlQuery = "SELECT d FROM Doctor d LEFT JOIN FETCH d.visits v LEFT JOIN FETCH v.patient p WHERE d.doctorId = :doctorId";
            TypedQuery<Doctor> query = entityManager.createQuery(jpqlQuery, Doctor.class);
            query.setParameter("doctorId", doctorId);

            Doctor doctor = query.getResultStream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Lekarz o id: " + doctorId + " nie zostal znaleziony"));


            List<LocalDate> bookedVisitDate = doctor.getVisits().stream()
                    .map(Visit::getVisitDate)
                    .collect(Collectors.toList());

            LocalDate availableVisitDate = startDate.plusDays(1);


            while (bookedVisitDate.contains(availableVisitDate)) {
                availableVisitDate = availableVisitDate.plusDays(1);
            }

            return availableVisitDate;
        }
    }



}
