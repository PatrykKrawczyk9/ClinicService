package pl.kurs.clinicservice;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import pl.kurs.clinicservice.config.BeansConfig;
import pl.kurs.clinicservice.dao.DoctorDao;
import pl.kurs.clinicservice.dao.IDoctorDao;
import pl.kurs.clinicservice.dao.IPatientDao;
import pl.kurs.clinicservice.dao.PatientDao;
import pl.kurs.clinicservice.io.DoctorFileTxtImport;
import pl.kurs.clinicservice.io.IFileDataImporter;
import pl.kurs.clinicservice.io.PatientFileTxtImport;
import pl.kurs.clinicservice.io.VisitFileTxtImport;
import pl.kurs.clinicservice.models.Patient;

import java.time.LocalDate;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeansConfig.class);

        IFileDataImporter doctorImport = context.getBean(DoctorFileTxtImport.class);
        IFileDataImporter patientImport = context.getBean(PatientFileTxtImport.class);
        IFileDataImporter visitImport = context.getBean(VisitFileTxtImport.class);

        doctorImport.importData();
        patientImport.importData();
        visitImport.importData();

        IPatientDao patientDao = context.getBean(PatientDao.class);
        IDoctorDao doctorDao = context.getBean(DoctorDao.class);

        //pozwoli pobrac pacjenta wraz z jego wszystkimi wizytami uzywajac jednego polecenia.
        Patient byIdWithVisits = patientDao.findByIdWithVisits(191L);
        byIdWithVisits.getVisits().forEach(System.out::println);


        //pozwoli znaleźć najbliższy wolny termin lekarza po podanym dnium (lekarze moze miec jedna wizyte dziennie powiedzmy)
        LocalDate firstAvailableVisitAfterDate = doctorDao.findFirstAvailableVisitAfterDate(23L, LocalDate.of(2006, 12, 12));
        System.out.println("firstAvailableVisitAfterDate = " + firstAvailableVisitAfterDate);


    }
}
