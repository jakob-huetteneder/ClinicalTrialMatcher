package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.service.TrialRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;


@RestController
@RequestMapping(path = TrialRegistrationEndpoint.BASE_PATH)
public class TrialRegistrationEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static final String BASE_PATH = TrialEndpoint.BASE_PATH + "/registration";

    private final TrialRegistrationService trialRegistrationService;

    public TrialRegistrationEndpoint(TrialRegistrationService trialRegistrationService) {
        this.trialRegistrationService = trialRegistrationService;
    }

    @Secured("ROLE_RESEARCHER")
    @GetMapping(value = "/{trialId}")
    public List<TrialRegistrationDto> getAllRegistrationsForTrial(@PathVariable("trialId") Long trialId) {
        LOG.info("Getting all registrations for trial with id {}", trialId);
        return trialRegistrationService.getAllRegistrationsForTrial(trialId);
    }

    @Secured("ROLE_PATIENT")
    @GetMapping
    public List<TrialRegistrationDto> getAllRegistrationsForLoggedInPatient() {
        LOG.info("Getting all registrations for logged in patient");
        return trialRegistrationService.getAllRegistrationsForPatient();
    }

    @Secured("ROLE_PATIENT")
    @PostMapping(value = "/{trialId}")
    public TrialRegistrationDto registerForTrialAsUser(@PathVariable("trialId") Long trialId) {
        LOG.info("Register for trial with id {}", trialId);
        return trialRegistrationService.requestRegistrationAsPatient(trialId);
    }

    @Secured("ROLE_DOCTOR")
    @PostMapping(value = "/{trialId}/patient/{patientId}")
    public TrialRegistrationDto registerPatientForTrialAsDoctor(@PathVariable("trialId") Long trialId, @PathVariable("patientId") Long patientId) {
        LOG.info("Register for trial with id {}", trialId);
        return trialRegistrationService.requestRegistrationAsDoctor(patientId, trialId);
    }

    @Secured("ROLE_PATIENT")
    @GetMapping(value = "/patient/{trialId}")
    public TrialRegistrationDto getRegistrationStatus(@PathVariable("trialId") Long trialId) {
        LOG.info("Check if already registered for trial with id {}", trialId);
        return trialRegistrationService.checkIfAlreadyRegistered(trialId);
    }

    @Secured("ROLE_PATIENT")
    @PutMapping(value = "/{trialId}/response")
    public TrialRegistrationDto respondToRegistrationRequestProposal(@PathVariable("trialId") Long trialId, @RequestBody Boolean accepted) {
        LOG.info("Respond to registration request for trial with id {}", trialId);
        return trialRegistrationService.respondToRegistrationRequestProposal(trialId, accepted);
    }

    @Secured("ROLE_RESEARCHER")
    @PutMapping(value = "/{trialId}/patient/{patientId}/response")
    public TrialRegistrationDto respondToRegistrationRequest(@PathVariable("trialId") Long trialId, @PathVariable("patientId") Long patientId, @RequestBody Boolean accepted) {
        LOG.info("Respond to registration request for trial with id {}", trialId);
        return trialRegistrationService.respondToRegistrationRequest(patientId, trialId, accepted);
    }
}
