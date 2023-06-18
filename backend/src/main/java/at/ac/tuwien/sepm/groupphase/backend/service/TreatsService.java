package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientRequestDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TreatsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;

import java.util.List;

/**
 * Service for managing treats relationships.
 */
public interface TreatsService {

    /**
     * Get all requests for a user (doctor or patient).
     *
     * @param userId the id of the user
     * @param search the search string
     * @return a list of all requests
     */
    List<TreatsDto> getAllRequests(long userId, String search);

    /**
     * Request a treats relationship with a patient.
     *
     * @param doctorId  the id of the doctor that wants to treat the patient
     * @param patientId the id of the patient that is requested
     * @return the created request
     */
    PatientRequestDto requestTreats(Long doctorId, Long patientId);

    /**
     * Respond to a treats request.
     *
     * @param patientUserId the id of the patient that is responding
     * @param doctorId      the id of the doctor that wants to treat the patient
     * @param accepted      whether the request was accepted or not
     * @return the updated treats relationship
     */
    TreatsDto respondToRequest(long patientUserId, Long doctorId, boolean accepted);

    /**
     * Create a treats relationship between a doctor and a patient.
     *
     * @param doctor  the doctor
     * @param patient the patient
     */
    void doctorTreatsPatient(Doctor doctor, Patient patient);

    /**
     * Delete a treats relationship between.
     *
     * @param sessionUserId the user id of the person wanting to delete the relationship
     * @param userId        the user id of the person to delete the relationship with
     */
    void deleteTreats(Long sessionUserId, Long userId);
}
