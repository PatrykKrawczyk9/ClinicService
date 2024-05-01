package pl.kurs.clinicservice.io;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.kurs.clinicservice.dao.IDoctorDao;
import pl.kurs.clinicservice.models.Doctor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorFileTxtImport implements IFileDataImporter {

    private IDoctorDao doctorDao;
    private String filePath;

    public DoctorFileTxtImport(IDoctorDao doctorDao, @Value("${file.path.doctors}") String filePath) {
        this.doctorDao = doctorDao;
        this.filePath = filePath;
    }

    @Override
    public void importData() {
        try {
            List<String> list = Files.readAllLines(Path.of(filePath));

            List<Doctor> doctors = list.stream()
                    .skip(1)
                    .filter(lines -> !lines.isEmpty())
                    .map(this::parseDoctor)
                    .collect(Collectors.toList());

            doctorDao.saveAll(doctors);
        } catch (IOException e) {
            System.err.println("Błąd importu danych: " + e.getMessage());
        }
    }

    private Doctor parseDoctor(String line) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        String[] split = line.split("\t");

        long doctorId = Long.parseLong(split[0]);
        String lastName = split[1];
        String firstName = split[2];
        String speciality = split[3];
        LocalDate birthDate = LocalDate.parse(split[4], formatter);
        String nip = split[5];
        String pesel = split[6];

        return new Doctor(lastName, firstName, birthDate, pesel, doctorId, speciality, nip);
    }
}
