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

    /**
     * Get all registrations for a trial.
     *
     * @param trialId id of trial
     * @return list of all registrations for a trial
     */
    @Secured({"ROLE_RESEARCHER", "ROLE_DOCTOR"})
    @GetMapping(value = "/{trialId}")
    public List<TrialRegistrationDto> getAllRegistrationsForTrial(@PathVariable("trialId") Long trialId) {
        LOG.trace("getAllRegistrationsForTrial({})", trialId);
        LOG.info("GET " + BASE_PATH + "/{}", trialId);
        return trialRegistrationService.getAllRegistrationsForTrial(trialId);
    }

    /**
     * Get all registrations of the logged in patient.
     *
     * @return list of all registrations of the logged in patient
     */
    @Secured("ROLE_PATIENT")
    @GetMapping
    public List<TrialRegistrationDto> getAllRegistrationsForLoggedInPatient() {
        LOG.trace("getAllRegistrationsForLoggedInPatient()");
        LOG.info("GET " + BASE_PATH);
        return trialRegistrationService.getAllRegistrationsForPatient();
    }

    /**
     * Register for a trial as a patient.
     *
     * @param trialId id of trial
     * @return registration
     */
    @Secured("ROLE_PATIENT")
    @PostMapping(value = "/{trialId}")
    public TrialRegistrationDto registerForTrialAsUser(@PathVariable("trialId") Long trialId) {
        LOG.trace("registerForTrialAsUser({})", trialId);
        LOG.info("POST " + BASE_PATH + "/{}", trialId);
        return trialRegistrationService.requestRegistrationAsPatient(trialId);
    }

    /**
     * Register a patient for a trial as a doctor.
     *
     * @param trialId   id of trial
     * @param patientId id of patient
     * @return registration
     */
    @Secured("ROLE_DOCTOR")
    @PostMapping(value = "/{trialId}/patient/{patientId}")
    public TrialRegistrationDto registerPatientForTrialAsDoctor(@PathVariable("trialId") Long trialId, @PathVariable("patientId") Long patientId) {
        LOG.trace("registerPatientForTrialAsDoctor({}, {})", trialId, patientId);
        LOG.info("POST " + BASE_PATH + "/{}/patient/{}", trialId, patientId);
        return trialRegistrationService.requestRegistrationAsDoctor(patientId, trialId);
    }

    /**
     * Get registration status of the logged in patient for a trial.
     *
     * @param trialId id of trial
     * @return registration status
     */
    @Secured("ROLE_PATIENT")
    @GetMapping(value = "/patient/{trialId}")
    public TrialRegistrationDto getRegistrationStatus(@PathVariable("trialId") Long trialId) {
        LOG.trace("getRegistrationStatus({})", trialId);
        LOG.info("GET " + BASE_PATH + "/patient/{}", trialId);
        return trialRegistrationService.checkIfAlreadyRegistered(trialId);
    }

    /**
     * Respond to the registration proposal of a doctor as a patient.
     *
     * @param trialId  id of trial to respond to
     * @param accepted true if accepted, false if declined
     * @return updated registration
     */
    @Secured("ROLE_PATIENT")
    @PutMapping(value = "/{trialId}/response")
    public TrialRegistrationDto respondToRegistrationRequestProposal(@PathVariable("trialId") Long trialId, @RequestBody Boolean accepted) {
        LOG.trace("respondToRegistrationRequestProposal({}, {})", trialId, accepted);
        LOG.info("PUT " + BASE_PATH + "/{}/response", trialId);
        return trialRegistrationService.respondToRegistrationRequestProposal(trialId, accepted);
    }

    /**
     * Respond to a registration request of a patient as a researcher.
     *
     * @param trialId   id of trial to respond to
     * @param patientId id of patient to respond to
     * @param accepted  true if accepted, false if declined
     * @return updated registration
     */
    @Secured("ROLE_RESEARCHER")
    @PutMapping(value = "/{trialId}/patient/{patientId}/response")
    public TrialRegistrationDto respondToRegistrationRequest(@PathVariable("trialId") Long trialId, @PathVariable("patientId") Long patientId, @RequestBody Boolean accepted) {
        LOG.trace("respondToRegistrationRequest({}, {}, {})", trialId, patientId, accepted);
        LOG.info("PUT " + BASE_PATH + "/{}/patient/{}/response", trialId, patientId);
        return trialRegistrationService.respondToRegistrationRequest(patientId, trialId, accepted);
    }
}
