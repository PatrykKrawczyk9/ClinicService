package pl.kurs.clinicservice.dao;

import pl.kurs.clinicservice.models.Visit;

import java.util.List;

public interface IVisitDao {
    Visit save(Visit visit);
    List<Visit> saveAll(List<Visit> visits);
    Visit findById(Long visitId);


}
