package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientDto;

public interface PatientService {

    PatientDto savePatient(PatientDto patient);
}
