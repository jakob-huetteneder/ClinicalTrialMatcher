package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialRegistrationDto;

import java.util.List;

public interface TrialRegistrationService {

    List<TrialRegistrationDto> getAllRegistrationsForTrial(Long trialId);

    List<TrialRegistrationDto> getAllRegistrationsForPatient();

    TrialRegistrationDto requestRegistrationAsPatient(Long trialId);

    TrialRegistrationDto requestRegistrationAsDoctor(Long patientId, Long trialId);

    TrialRegistrationDto respondToRegistrationRequestProposal(Long trialId, boolean accepted);

    TrialRegistrationDto respondToRegistrationRequest(Long patientId, Long trialId, boolean accepted);

    TrialRegistrationDto checkIfAlreadyRegistered(Long trialId);
}
