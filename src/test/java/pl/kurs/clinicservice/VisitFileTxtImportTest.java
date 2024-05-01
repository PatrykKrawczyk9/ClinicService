package pl.kurs.clinicservice;

import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.kurs.clinicservice.dao.IDoctorDao;
import pl.kurs.clinicservice.dao.IPatientDao;
import pl.kurs.clinicservice.dao.IVisitDao;
import pl.kurs.clinicservice.io.VisitFileTxtImport;
import pl.kurs.clinicservice.models.Doctor;
import pl.kurs.clinicservice.models.Patient;
import pl.kurs.clinicservice.models.Visit;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VisitFileTxtImportTest {

    @Mock
    IVisitDao visitDao;
    @Mock
    IDoctorDao doctorDao;
    @Mock
    IPatientDao patientDao;
    VisitFileTxtImport service;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldSaveAllVisitFromFile() {
        //given
        String filePath = "src/test/resources/wizyty-test.txt";
        service = new VisitFileTxtImport(visitDao, doctorDao, patientDao, filePath);
        Doctor doctorDaoMock = new Doctor();
        Patient patientDaoMock = new Patient();
        when(patientDao.findById(1L)).thenReturn(patientDaoMock);
        when(doctorDao.findById(1L)).thenReturn(doctorDaoMock);

        //when
        service.importData();

        //then
        verify(visitDao).saveAll(anyList());
    }

    @Test
    public void shouldCorrectParseVisit() {
        //given
        SoftAssertions sa = new SoftAssertions();
        String filePath = "src/test/resources/wizyty-test.txt";
        service = new VisitFileTxtImport(visitDao, doctorDao, patientDao, filePath);
        Doctor doctorDaoMock = new Doctor();
        doctorDaoMock.setDoctorId(1L);
        Patient patientDaoMock = new Patient();
        patientDaoMock.setPatientId(1L);
        when(patientDao.findById(1L)).thenReturn(patientDaoMock);
        when(doctorDao.findById(1L)).thenReturn(doctorDaoMock);

        //when
        service.importData();

        //then
        ArgumentCaptor<List<Visit>> visitCaptor = ArgumentCaptor.forClass(List.class);
        verify(visitDao).saveAll(visitCaptor.capture());

        List<Visit> value = visitCaptor.getValue();
        Visit visit = value.get(0);

        sa.assertThat(visit.getDoctor().getDoctorId()).isEqualTo(1L);
        sa.assertThat(visit.getPatient().getPatientId()).isEqualTo(1L);
        sa.assertThat(visit.getVisitDate()).isEqualTo("2006-12-13");
        sa.assertAll();
    }

}