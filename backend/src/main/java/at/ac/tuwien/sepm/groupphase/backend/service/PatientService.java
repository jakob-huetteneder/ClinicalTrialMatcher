package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ExaminationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;

public interface PatientService {

    /**
     * Find a patient based on their id.
     *
     * @param id the id
     * @return a patient
     */
    Patient patientGetById(Long id);

    // TODO: Add other Patient methods and implement them in PatientDetailService
}
