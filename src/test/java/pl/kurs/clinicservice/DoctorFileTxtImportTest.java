package pl.kurs.clinicservice;

import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.kurs.clinicservice.dao.IDoctorDao;
import pl.kurs.clinicservice.io.DoctorFileTxtImport;
import pl.kurs.clinicservice.models.Doctor;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;

public class DoctorFileTxtImportTest {

    @Mock
    IDoctorDao doctorDaoMock;
    DoctorFileTxtImport service;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldSaveAllDoctorsFromFile() {
        //given
        String filePath = "src/test/resources/lekarze-test.txt";
        service = new DoctorFileTxtImport(doctorDaoMock, filePath);

        //when
        service.importData();

        //then
        verify(doctorDaoMock).saveAll(anyList());
    }

    @Test
    public void shouldCorrectParseDoctor() {
        //given
        SoftAssertions sa = new SoftAssertions();
        String filePath = "src/test/resources/lekarze-test.txt";
        service = new DoctorFileTxtImport(doctorDaoMock, filePath);

        //when
        service.importData();

        //then
        ArgumentCaptor<List<Doctor>> doctorCaptor = ArgumentCaptor.forClass(List.class);
        verify(doctorDaoMock).saveAll(doctorCaptor.capture());

        List<Doctor> doctors = doctorCaptor.getValue();
        Doctor doctor = doctors.get(0);

        sa.assertThat(doctor).isNotNull();
        sa.assertThat(doctor.getFirstName()).isEqualTo("Michalski");
        sa.assertThat(doctor.getLastName()).isEqualTo("Patryk");
        sa.assertThat(doctor.getSpecialty()).isEqualTo("neurochirurg");
        sa.assertThat(doctor.getBirthDate()).isEqualTo("1985-09-22");
        sa.assertThat(doctor.getNip()).isEqualTo("443-648-27-61");
        sa.assertThat(doctor.getPesel()).isEqualTo("85092263492");
        sa.assertAll();
    }


}