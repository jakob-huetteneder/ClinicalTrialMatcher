package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientRequestDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;

import java.time.LocalDate;
import java.util.List;

/**
 * Service for managing patients.
 */
public interface PatientService {

    /**
     * Save a patient.
     *
     * @param patient to save
     * @return saved patient
     */
    PatientDto savePatient(PatientDto patient);

    /**
     * Get a patient by id.
     *
     * @param id of the patient
     * @return patient with the given id
     */
    PatientDto getById(long id);

    /**
     * Get all patients treated by a given doctor.
     *
     * @param doctorId id of the doctor
     * @param search   search string
     * @return list of patients
     */
    List<PatientRequestDto> getAllPatientsForDoctorId(Long doctorId, String search);

    /**
     * Delete a patient by id.
     *
     * @param id of the patient
     * @return deleted patient
     */
    PatientDto deleteById(long id);

    /**
     * Return all patients that match with the given trial
     *
     * @param inclusion list of inclusion criteria
     * @param exclusion list of exclusion criteria
     * @param minAge    minimum age requirement
     * @param maxAge    maximum age requirement
     * @param gender    required gender
     * @return list of patients that match with the given trial
     */
    List<PatientDto> matchPatientsWithTrial(List<String> inclusion, List<String> exclusion, LocalDate minAge, LocalDate maxAge, Gender gender);

    /**
     * Synchronize the patient with the given id with the elastic search database
     *
     * @param id of the patient
     * @return the synchronized patient
     */
    PatientDto synchronizeWithElasticSearchDb(long id);
}
