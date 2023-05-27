package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientRequestDto;

import java.util.List;

public interface PatientService {

    PatientDto savePatient(PatientDto patient);

    PatientDto getById(long id);

    List<PatientRequestDto> getAllPatientsForDoctorId(Long doctorId);

    PatientDto deleteById(long id);

}
