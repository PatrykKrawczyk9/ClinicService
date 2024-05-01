package pl.kurs.clinicservice.dao;

import pl.kurs.clinicservice.models.Doctor;

import java.time.LocalDate;
import java.util.List;

public interface IDoctorDao {
    Doctor save(Doctor doctor);
    List<Doctor> saveAll(List<Doctor> doctors);
    Doctor findById(Long doctorId);
    LocalDate findFirstAvailableVisitAfterDate(Long doctorId, LocalDate startDate);
}
