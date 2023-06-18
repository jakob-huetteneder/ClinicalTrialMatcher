package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialRegistrationDto;

import java.util.List;

public interface TrialRegistrationService {

    /**
     * Get all registrations for a trial.
     *
     * @param trialId id of trial
     * @return list of all registrations for a trial
     */
    List<TrialRegistrationDto> getAllRegistrationsForTrial(Long trialId);

    /**
     * Get all registrations of the logged in patient.
     *
     * @return list of all registrations of the logged in patient
     */
    List<TrialRegistrationDto> getAllRegistrationsForPatient();

    /**
     * Request registration for a trial as a patient.
     *
     * @param trialId id of trial
     * @return the registration as a dto
     */
    TrialRegistrationDto requestRegistrationAsPatient(Long trialId);

    /**
     * Request registration for a trial as a doctor.
     *
     * @param patientId id of patient
     * @param trialId   id of trial
     * @return the registration as a dto
     */
    TrialRegistrationDto requestRegistrationAsDoctor(Long patientId, Long trialId);

    /**
     * Respond to a registration request proposal as a patient.
     *
     * @param trialId  id of trial
     * @param accepted true if accepted, false if rejected
     * @return the registration as a dto
     */
    TrialRegistrationDto respondToRegistrationRequestProposal(Long trialId, boolean accepted);

    /**
     * Respond to a registration request as a researcher.
     *
     * @param patientId id of patient
     * @param trialId   id of trial
     * @param accepted  true if accepted, false if rejected
     * @return the registration as a dto
     */
    TrialRegistrationDto respondToRegistrationRequest(Long patientId, Long trialId, boolean accepted);

    /**
     * Get registration status of the logged in patient for a trial.
     *
     * @param trialId id of trial
     * @return registration status
     */
    TrialRegistrationDto checkIfAlreadyRegistered(Long trialId);
}
