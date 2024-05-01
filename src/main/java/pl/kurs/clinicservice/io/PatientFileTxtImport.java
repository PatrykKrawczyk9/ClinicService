package pl.kurs.clinicservice.io;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.kurs.clinicservice.dao.IPatientDao;
import pl.kurs.clinicservice.models.Patient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientFileTxtImport implements IFileDataImporter {

    private IPatientDao patientDao;
    private String filePath;

    public PatientFileTxtImport(IPatientDao patientDao, @Value("${file.path.patients}") String filePath) {
        this.patientDao = patientDao;
        this.filePath = filePath;
    }

    @Override
    public void importData() {
        try {
            List<String> list = Files.readAllLines(Path.of(filePath));

            List<Patient> patients = list.stream()
                    .skip(1)
                    .filter(lines -> !lines.isEmpty())
                    .map(this::parsePatient)
                    .collect(Collectors.toList());

            patientDao.saveAll(patients);
        } catch (IOException e) {
            System.err.println("Błąd importu danych: " + e.getMessage());
        }
    }

    private Patient parsePatient(String line) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        String[] split = line.split("\t");

        long patientId = Long.parseLong(split[0]);
        String lastName = split[1];
        String firstName = split[2];
        String pesel = split[3];
        LocalDate birthDate = LocalDate.parse(split[4], formatter);

        return new Patient(lastName, firstName, birthDate, pesel, patientId);
    }
}
