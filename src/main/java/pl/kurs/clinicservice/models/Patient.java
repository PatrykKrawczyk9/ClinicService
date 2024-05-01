package pl.kurs.clinicservice.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "patients")
public class Patient extends Person {

    @Id
    @Column(unique = true, nullable = false)
    private Long patientId;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Visit> visits = new HashSet<>();

    public Patient() {
    }

    public Patient(String lastName, String firstName, LocalDate birthDate, String pesel, Long patientId) {
        super(lastName, firstName, birthDate, pesel);
        this.patientId = patientId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Set<Visit> getVisits() {
        return visits;
    }

    public void setVisits(Set<Visit> visits) {
        this.visits = visits;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
