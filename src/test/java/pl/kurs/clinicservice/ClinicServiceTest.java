package pl.kurs.clinicservice;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import pl.kurs.clinicservice.config.BeansConfig;
import pl.kurs.clinicservice.dao.IDoctorDao;
import pl.kurs.clinicservice.dao.IPatientDao;
import pl.kurs.clinicservice.dao.VisitDao;
import pl.kurs.clinicservice.models.Doctor;
import pl.kurs.clinicservice.models.Patient;
import pl.kurs.clinicservice.models.Visit;

import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {BeansConfig.class},
        loader = AnnotationConfigContextLoader.class)
@Sql(scripts = "classpath:test-skrypt.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ClinicServiceTest {

    @Resource
    IPatientDao patientDao;
    @Resource
    IDoctorDao doctorDao;
    @Resource
    VisitDao visitDao;

    @Test
    public void shouldCorrectSavePatient() {
        //given
        SoftAssertions sa = new SoftAssertions();
        Patient patient1 = new Patient("Michał", "Inny",
                LocalDate.of(2000, 4, 22), "123456", 3L);

        //when
        Patient savedPatient = patientDao.save(patient1);

        //then
        sa.assertThat(savedPatient.getLastName()).isEqualTo("Michał");
        sa.assertThat(savedPatient.getFirstName()).isEqualTo("Inny");
        sa.assertThat(savedPatient.getBirthDate()).isEqualTo("2000-04-22");
        sa.assertThat(savedPatient.getPesel()).isEqualTo("123456");
        sa.assertThat(savedPatient.getPatientId()).isEqualTo(3L);
        sa.assertAll();
    }

    @Test
    public void shouldCorrectSaveDoctor() {
        //given
        SoftAssertions sa = new SoftAssertions();
        Doctor doctor1 = new Doctor("Brzęczyszczykiewicz", "Katarzyna",
                LocalDate.of(1978, 1, 1), "12341234", 3L, "neurochirug", "43214321");

        //when
        Doctor savedDoctor = doctorDao.save(doctor1);

        //then
        sa.assertThat(savedDoctor.getLastName()).isEqualTo("Brzęczyszczykiewicz");
        sa.assertThat(savedDoctor.getFirstName()).isEqualTo("Katarzyna");
        sa.assertThat(savedDoctor.getBirthDate()).isEqualTo("1978-01-01");
        sa.assertThat(savedDoctor.getPesel()).isEqualTo("12341234");
        sa.assertThat(savedDoctor.getDoctorId()).isEqualTo(3L);
        sa.assertThat(savedDoctor.getSpecialty()).isEqualTo("neurochirug");
        sa.assertThat(savedDoctor.getNip()).isEqualTo("43214321");
        sa.assertAll();
    }

    @Test
    public void shouldCorrectSaveVisit() {
        //given
        SoftAssertions sa = new SoftAssertions();
        Doctor doctor = new Doctor("Kordylewski", "Michal",
                LocalDate.of(1970, 1, 13), "5670985566", 2L, "nefrolog", "70011345567");
        Patient patient = new Patient("Nowak", "Anna",
                LocalDate.of(1985, 5, 10), "23456789012", 2L);
        Visit visit = new Visit(doctor, patient, LocalDate.of(2024, 5, 5));

        //when
        Visit savedVisit = visitDao.save(visit);

        //then
        sa.assertThat(savedVisit.getDoctor().getLastName()).isEqualTo("Kordylewski");
        sa.assertThat(savedVisit.getDoctor().getFirstName()).isEqualTo("Michal");
        sa.assertThat(savedVisit.getDoctor().getBirthDate()).isEqualTo("1970-01-13");
        sa.assertThat(savedVisit.getDoctor().getNip()).isEqualTo("70011345567");
        sa.assertThat(savedVisit.getDoctor().getSpecialty()).isEqualTo("nefrolog");
        sa.assertThat(savedVisit.getDoctor().getDoctorId()).isEqualTo(2L);

        sa.assertThat(savedVisit.getPatient().getLastName()).isEqualTo("Nowak");
        sa.assertThat(savedVisit.getPatient().getFirstName()).isEqualTo("Anna");
        sa.assertThat(savedVisit.getPatient().getBirthDate()).isEqualTo("1985-05-10");
        sa.assertThat(savedVisit.getPatient().getPesel()).isEqualTo("23456789012");
        sa.assertThat(savedVisit.getPatient().getPatientId()).isEqualTo(2L);

        sa.assertThat(savedVisit.getVisitDate()).isEqualTo(LocalDate.of(2024, 5, 5));

        sa.assertAll();
    }

    @Test
    public void shouldReturnPatientFromId() {
        //given
        SoftAssertions sa = new SoftAssertions();

        //when
        Patient foundedPatient = patientDao.findById(1L);

        //then
        sa.assertThat(foundedPatient.getFirstName()).isEqualTo("Jan");
        sa.assertThat(foundedPatient.getLastName()).isEqualTo("Kowalski");
        sa.assertThat(foundedPatient.getBirthDate()).isEqualTo("1995-12-10");
        sa.assertThat(foundedPatient.getPesel()).isEqualTo("123456789");
        sa.assertThat(foundedPatient.getPatientId()).isEqualTo(1L);
        sa.assertAll();
    }

    @Test
    public void shouldReturnDoctorFromId() {
        //given
        SoftAssertions sa = new SoftAssertions();

        //when
        Doctor foundedDoctor = doctorDao.findById(1L);

        //then
        sa.assertThat(foundedDoctor.getFirstName()).isEqualTo("Joanna");
        sa.assertThat(foundedDoctor.getLastName()).isEqualTo("Michalska");
        sa.assertThat(foundedDoctor.getBirthDate()).isEqualTo("1956-02-02");
        sa.assertThat(foundedDoctor.getNip()).isEqualTo("12345257792");
        sa.assertThat(foundedDoctor.getPesel()).isEqualTo("987123478");
        sa.assertThat(foundedDoctor.getSpecialty()).isEqualTo("neurochirug");
        sa.assertThat(foundedDoctor.getDoctorId()).isEqualTo(1L);
        sa.assertAll();
    }

    @Test
    public void shouldReturnVisitFromId() {
        //given
        SoftAssertions sa = new SoftAssertions();

        //when
        Visit foundedVisit = visitDao.findById(1L);

        //then
        Doctor doctor = foundedVisit.getDoctor();
        Patient patient = foundedVisit.getPatient();

        sa.assertThat(doctor.getFirstName()).isEqualTo("Joanna");
        sa.assertThat(doctor.getLastName()).isEqualTo("Michalska");
        sa.assertThat(doctor.getBirthDate()).isEqualTo("1956-02-02");
        sa.assertThat(doctor.getNip()).isEqualTo("12345257792");
        sa.assertThat(doctor.getPesel()).isEqualTo("987123478");
        sa.assertThat(doctor.getSpecialty()).isEqualTo("neurochirug");
        sa.assertThat(doctor.getDoctorId()).isEqualTo(1L);

        sa.assertThat(patient.getFirstName()).isEqualTo("Jan");
        sa.assertThat(patient.getLastName()).isEqualTo("Kowalski");
        sa.assertThat(patient.getBirthDate()).isEqualTo("1995-12-10");
        sa.assertThat(patient.getPesel()).isEqualTo("123456789");
        sa.assertThat(patient.getPatientId()).isEqualTo(1L);

        sa.assertThat(foundedVisit.getVisitDate()).isEqualTo(LocalDate.of(2024, 5, 2));

        sa.assertAll();
    }

    @Test
    public void shouldFindPatientFromIdWithVisits() {
        //given
        SoftAssertions sa = new SoftAssertions();

        //when
        Patient patient = patientDao.findByIdWithVisits(1L);

        //then
        sa.assertThat(patient.getFirstName()).isEqualTo("Jan");
        sa.assertThat(patient.getLastName()).isEqualTo("Kowalski");
        sa.assertThat(patient.getBirthDate()).isEqualTo("1995-12-10");
        sa.assertThat(patient.getPesel()).isEqualTo("123456789");
        sa.assertThat(patient.getPatientId()).isEqualTo(1L);

        sa.assertThat(patient.getVisits().size()).isEqualTo(1);
        sa.assertThat(patient.getVisits().contains(visitDao.findById(1L))).isTrue();
        sa.assertAll();
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenPatientNotFound() {
        // given
        Long nonExistingPatientId = 100L;

        // then
        Exception e = assertThrows(EntityNotFoundException.class, () -> patientDao.findByIdWithVisits(nonExistingPatientId));
        assertNotNull(e);
        assertEquals("Pacjent o id: 100 nie zostal znaleziony", e.getMessage());
    }

    @Test
    public void shouldReturnFirstAvailableVisitAfterDate() {
        // given
        Long doctorId = 1L;
        LocalDate startDate = LocalDate.of(2024, 5, 1);
        LocalDate firstAvailableDate = LocalDate.of(2024, 5, 3);

        // when
        LocalDate availableVisitDate = doctorDao.findFirstAvailableVisitAfterDate(doctorId, startDate);

        // then
        assertEquals(firstAvailableDate, availableVisitDate);
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenDoctorNotFound() {
        // given
        Long nonExistingDoctorId = 100L;
        LocalDate startDate = LocalDate.of(2024, 12, 12);

        // then
        Exception e = assertThrows(EntityNotFoundException.class, () -> doctorDao.findFirstAvailableVisitAfterDate(nonExistingDoctorId, startDate));
        assertNotNull(e);
        assertEquals("Lekarz o id: 100 nie zostal znaleziony", e.getMessage());
    }
}