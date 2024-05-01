package pl.kurs.clinicservice;

import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.kurs.clinicservice.dao.IPatientDao;
import pl.kurs.clinicservice.io.PatientFileTxtImport;
import pl.kurs.clinicservice.models.Patient;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;

public class PatientFileTxtImportTest {

    @Mock
    IPatientDao patientDaoMock;
    PatientFileTxtImport service;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldSaveAllPatientsFromFile() {
        //given
        String filePath = "src/test/resources/pacjenci-test.txt";
        service = new PatientFileTxtImport(patientDaoMock, filePath);

        //when
        service.importData();

        //then
        verify(patientDaoMock).saveAll(anyList());
    }

    @Test
    public void shouldCorrectParsePatient() {
        //given
        SoftAssertions sa = new SoftAssertions();
        String filePath = "src/test/resources/pacjenci-test.txt";
        service = new PatientFileTxtImport(patientDaoMock, filePath);

        //when
        service.importData();

        //then
        ArgumentCaptor<List<Patient>> patientCaptor = ArgumentCaptor.forClass(List.class);
        verify(patientDaoMock).saveAll(patientCaptor.capture());

        List<Patient> patients = patientCaptor.getValue();
        Patient patient = patients.get(0);

        sa.assertThat(patient.getLastName()).isEqualTo("Kowalski");
        sa.assertThat(patient.getFirstName()).isEqualTo("Jan");
        sa.assertThat(patient.getPesel()).isEqualTo("93052383755");
        sa.assertThat(patient.getBirthDate()).isEqualTo("1956-03-22");
        sa.assertAll();
    }

}