package pl.kurs.clinicservice.io;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.kurs.clinicservice.dao.IDoctorDao;
import pl.kurs.clinicservice.dao.IPatientDao;
import pl.kurs.clinicservice.dao.IVisitDao;
import pl.kurs.clinicservice.models.Doctor;
import pl.kurs.clinicservice.models.Patient;
import pl.kurs.clinicservice.models.Visit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VisitFileTxtImport implements IFileDataImporter {

    private IVisitDao visitDao;
    private IDoctorDao doctorDao;
    private IPatientDao patientDao;
    private String filePath;

    public VisitFileTxtImport(IVisitDao visitDao, IDoctorDao doctorDao, IPatientDao patientDao, @Value("${file.path.visits}") String filePath) {
        this.visitDao = visitDao;
        this.doctorDao = doctorDao;
        this.patientDao = patientDao;
        this.filePath = filePath;
    }

    @Override
    public void importData() {
        try {
            List<String> list = Files.readAllLines(Path.of(filePath));

            List<Visit> visits = list.stream()
                    .skip(1)
                    .filter(lines -> !lines.isEmpty())
                    .map(this::parseVisit)
                    .collect(Collectors.toList());

            visitDao.saveAll(visits);
        } catch (IOException e) {
            System.err.println("Błąd importu danych: " + e.getMessage());
        }
    }

    private Visit parseVisit(String line) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        String[] split = line.split("\t");

        long doctorId = Long.parseLong(split[0]);
        long patientId = Long.parseLong(split[1]);

        LocalDate visitDate = LocalDate.parse(split[2], formatter);

        Doctor doctor = Optional.ofNullable(doctorDao.findById(doctorId)).orElseThrow(EntityNotFoundException::new);
        Patient patient = Optional.ofNullable(patientDao.findById(patientId)).orElseThrow(EntityNotFoundException::new);

        return new Visit(doctor, patient, visitDate);
    }
}
