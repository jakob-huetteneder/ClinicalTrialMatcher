package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiagnoseDto;

import java.util.List;

public interface DiagnoseService {

    /**
     * Add Diagnosis for a Patient.
     *
     * @param diagnoseDto the diagnosis
     * @return the created diagnosis object
     */
    DiagnoseDto addNewDiagnosis(DiagnoseDto diagnoseDto);

    /**
     * Update a certain diagnosis.
     *
     * @param diagnoseDto the diagnosis
     * @return the created examination object
     */
    DiagnoseDto updateDiagnosis(DiagnoseDto diagnoseDto);

    /**
     * Delete a certain diagnosis.
     *
     * @param id the id of the patient
     * @param diagnosisId the id of the diagnosis
     * @return the deleted examination object
     */
    DiagnoseDto deleteDiagnosis(long id, long diagnosisId);

    /**
     * View a certain diagnosis.
     *
     * @param id the id of the patient
     * @param diagnosisId the id of the diagnosis
     * @return the viewed examination object
     */
    DiagnoseDto viewDiagnosis(long id, long diagnosisId);

    /**
     * Get all diagnoses for a patient.
     *
     * @param id the id of the patient
     * @return a list of all diagnoses
     */
    List<DiagnoseDto> getAllDiagnoses(long id);
}
