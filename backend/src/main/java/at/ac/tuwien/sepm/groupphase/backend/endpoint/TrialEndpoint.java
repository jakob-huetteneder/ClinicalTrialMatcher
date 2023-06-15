package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.PatientServiceImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.TrialServiceImpl;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.List;

/**
 * This class defines the endpoints for the trial resource.
 */
@RestController
@RequestMapping(path = TrialEndpoint.BASE_PATH)
public class TrialEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static final String BASE_PATH = "/api/v1/trials";
    private final TrialServiceImpl trialService;
    private final PatientServiceImpl patientService;

    public TrialEndpoint(TrialServiceImpl trialService, PatientServiceImpl patientService
    ) {
        this.trialService = trialService;
        this.patientService = patientService;
    }

    /**
     * Adds a new trial to the database.
     *
     * @param trial to be added
     * @return the added trial
     */
    @Secured("ROLE_RESEARCHER")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TrialDto saveTrial(@RequestBody @Valid TrialDto trial) {
        LOG.trace("saveTrial({})", trial);
        LOG.info("POST " + BASE_PATH + "/");

        return trialService.saveTrial(trial);
    }

    /**
     * Returns the trial with the given id.
     *
     * @param id of the trial to be returned
     * @return the trial with the given id
     */
    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "{id}")
    public TrialDto findTrialById(@PathVariable("id") Long id) {
        LOG.trace("findTrialById({})", id);
        LOG.info("GET " + BASE_PATH + "/{}", id);
        return trialService.findTrialById(id);
    }

    /**
     * Returns all patients that match the given trial.
     *
     * @param id of the trial to be matched
     * @return all patients that match the given trial
     */
    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/match/{id}")
    public List<PatientDto> matchByTrialId(@PathVariable("id") Long id) {
        LOG.trace("matchByTrialId({})", id);
        LOG.info("GET " + BASE_PATH + "/match/{}", id);
        TrialDto trial = trialService.findTrialById(id);
        Gender gender = trial.crGender();
        LocalDate now = LocalDate.now();
        LocalDate minAge = LocalDate.of(now.getYear() - trial.crMinAge(), now.getMonth(), now.getDayOfMonth());
        LocalDate maxAge = LocalDate.of(now.getYear() - trial.crMaxAge(), now.getMonth(), now.getDayOfMonth());
        return patientService.matchPatientsWithTrial(trial.inclusionCriteria(), trial.exclusionCriteria(), minAge, maxAge, gender);
    }

    /**
     * Returns all trials that the current user (researcher) is responsible for.
     *
     * @return all trials that the current user (researcher) is responsible for
     */
    @Secured("ROLE_RESEARCHER")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/researcher")
    public List<TrialDto> getOwnTrials() {
        LOG.trace("getOwnTrials()");
        LOG.info("GET " + BASE_PATH + "/researcher");
        return trialService.getOwnTrials();
    }

    /**
     * Returns all trials.
     *
     * @return all trials
     */
    @Secured("ROLE_RESEARCHER")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public List<TrialDto> getAllTrials() {
        LOG.trace("getAllTrials()");
        LOG.info("GET " + BASE_PATH + "/");
        return trialService.getAllTrials();
    }

    /**
     * Updates the given trial.
     *
     * @param toUpdate trial to be updated
     * @return the updated trial
     */
    @Secured("ROLE_RESEARCHER")
    @PutMapping("{id}")
    public TrialDto update(@RequestBody @Valid TrialDto toUpdate) {
        LOG.trace("update({})", toUpdate);
        LOG.info("PUT " + BASE_PATH + "/{}", toUpdate);
        LOG.debug("Body of request:{}", toUpdate);
        return trialService.updateTrial(toUpdate);
    }

    /**
     * Deletes the trial with the given id.
     *
     * @param id of the trial to be deleted
     */
    @Secured("ROLE_RESEARCHER")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        LOG.trace("delete({})", id);
        LOG.info("DELETE " + BASE_PATH + "/{}", id);
        LOG.debug("Body of request:{}", id);
        trialService.deleteTrialById(id);
    }

}
