package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientRequestDto;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.PatientServiceImpl;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * This class defines the endpoints for the patient resource.
 */
@RestController
@RequestMapping(path = PatientEndpoint.BASE_PATH)
public class PatientEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static final String BASE_PATH = "/api/v1/patients";
    private final PatientServiceImpl patientService;
    private final AuthorizationService authorizationService;

    public PatientEndpoint(PatientServiceImpl patientService, AuthorizationService authorizationService) {
        this.patientService = patientService;
        this.authorizationService = authorizationService;
    }

    /**
     * Adds a new patient to the database.
     *
     * @param patient to be added
     * @return the added patient
     */
    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public PatientDto savePatient(@RequestBody @Valid PatientDto patient) {
        LOG.trace("savePatient({})", patient);
        LOG.info("POST " + BASE_PATH + "/");
        LOG.debug("Body of request: {}", patient);
        return patientService.savePatient(patient);
    }

    /**
     * Returns the patient with the given id.
     *
     * @param id of the patient to find
     * @return the patient with the given id
     */
    @PermitAll
    @GetMapping("{id}")
    public PatientDto getById(@PathVariable long id) {
        LOG.trace("getById({})", id);
        LOG.info("GET " + BASE_PATH + "/{}", id);
        return patientService.getById(id);
    }

    /**
     * Returns all patients matching the search string.
     *
     * @param search search string
     * @return all patients matching the search string
     */
    @Secured("ROLE_DOCTOR")
    @GetMapping
    public List<PatientRequestDto> getAll(@Param("search") String search) {
        LOG.trace("getAll({})", search);
        LOG.info("GET " + BASE_PATH + "/?search={}", search);
        Long doctorId = authorizationService.getSessionUserId();
        List<PatientRequestDto> patients = patientService.getAllPatientsForDoctorId(doctorId, search);
        LOG.debug("Found {} patients", patients.size());
        return patients;
    }

    /**
     * Deletes the patient with the given id.
     *
     * @param id of the patient to delete
     * @return the deleted patient
     */
    @PermitAll
    @DeleteMapping("{id}")
    public PatientDto deleteById(@PathVariable long id) {
        LOG.trace("deleteById({})", id);
        LOG.info("DELETE " + BASE_PATH + "/{}", id);
        return patientService.deleteById(id);
    }
}
