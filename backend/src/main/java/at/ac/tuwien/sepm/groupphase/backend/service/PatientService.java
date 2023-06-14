package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientRequestDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public interface PatientService {

    PatientDto savePatient(PatientDto patient);

    PatientDto getById(long id);

    List<PatientRequestDto> getAllPatientsForDoctorId(Long doctorId, String search);

    PatientDto deleteById(long id);

    List<PatientDto> matchPatientsWithTrial(List<String> inclusion, List<String> exclusion, LocalDate minAge, LocalDate maxAge, Gender gender);

}
