package pl.kurs.clinicservice.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "doctors")
public class Doctor extends Person {

    @Id
    @Column(unique = true, nullable = false)
    private Long doctorId;

    @Column(nullable = false)
    private String specialty;

    @Column(unique = true, nullable = false)
    private String nip;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Visit> visits = new HashSet<>();

    public Doctor() {
    }

    public Doctor(String lastName, String firstName, LocalDate birthDate, String pesel, Long doctorId, String specialty, String nip) {
        super(lastName, firstName, birthDate, pesel);
        this.doctorId = doctorId;
        this.specialty = specialty;
        this.nip = nip;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public Set<Visit> getVisits() {
        return visits;
    }

    public void setVisits(Set<Visit> visits) {
        this.visits = visits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(doctorId, doctor.doctorId) && Objects.equals(specialty, doctor.specialty) && Objects.equals(nip, doctor.nip) && Objects.equals(visits, doctor.visits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorId, specialty, nip, visits);
    }

    @Override
    public String toString() {
        return super.toString() +
                ", specialty='" + specialty + '\'' +
                ", nip='" + nip + '\'' +
                '}';
    }
}
