package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialRegistrationDto;

import java.util.List;

public interface TrialRegistrationService {

    List<TrialRegistrationDto> getAllRegistrationsForTrial(Long trialId);

    TrialRegistrationDto requestRegistrationAsPatient(Long trialId);

    TrialRegistrationDto requestRegistrationAsDoctor(Long patientId, Long trialId);

    TrialRegistrationDto respondToRegistrationRequestProposal(Long trialId, boolean accepted);

    TrialRegistrationDto respondToRegistrationRequest(Long patientId, Long trialId, boolean accepted);

    boolean checkIfAlreadyRegistered(Long trialId);
}
