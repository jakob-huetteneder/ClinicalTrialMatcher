package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiagnoseDto;
import at.ac.tuwien.sepm.groupphase.backend.service.DiagnoseService;
import at.ac.tuwien.sepm.groupphase.backend.service.PatientService;
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
import java.util.List;

/**
 * This class defines the endpoints for the diagnose resource.
 */
@RestController
@RequestMapping(path = DiagnoseEndpoint.BASE_PATH)
public class DiagnoseEndpoint {

    static final String BASE_PATH = "/api/v1/patients";
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final DiagnoseService diagnoseService;
    private final PatientService patientService;


    public DiagnoseEndpoint(DiagnoseService diagnoseService, PatientService patientService) {
        this.diagnoseService = diagnoseService;
        this.patientService = patientService;
    }

    /**
     * Adds a new diagnosis to the database for the patient with the given id.
     *
     * @param id          of the patient
     * @param diagnoseDto to be added
     * @return the added diagnosis
     */
    @Secured({"ROLE_DOCTOR"})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/{id}/diagnose")
    public DiagnoseDto addNewDiagnosis(@PathVariable("id") long id, @RequestBody @Valid DiagnoseDto diagnoseDto) {
        LOG.trace("addNewDiagnosis({}, {})", id, diagnoseDto);
        LOG.info("POST " + BASE_PATH + "/");
        LOG.debug("Body of request: {}", diagnoseDto);
        DiagnoseDto dto = diagnoseService.addNewDiagnosis(diagnoseDto.withPatientId(id));
        patientService.synchronizeWithElasticSearchDb(dto.patientId());
        return dto;
    }

    /**
     * Updates the diagnosis with the given id for the patient with the given id.
     *
     * @param id          of the patient
     * @param diagnosisId of the diagnosis
     * @param diagnoseDto to be updated
     * @return the updated diagnosis
     */
    @Secured({"ROLE_DOCTOR"})
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/{id}/diagnose/{d_id}")
    public DiagnoseDto updateDiagnosis(@PathVariable("id") long id, @PathVariable("d_id") long diagnosisId, @RequestBody @Valid DiagnoseDto diagnoseDto) {
        LOG.trace("updateDiagnosis({}, {})", id, diagnoseDto);
        LOG.info("POST " + BASE_PATH + "/");
        LOG.debug("Body of request: {}", diagnoseDto);
        DiagnoseDto dto = diagnoseService.updateDiagnosis(diagnoseDto.withDiagnosisId(diagnosisId).withPatientId(id));
        patientService.synchronizeWithElasticSearchDb(dto.patientId());
        return dto;
    }

    /**
     * Deletes the diagnosis with the given id for the patient with the given id.
     *
     * @param id          of the patient
     * @param diagnosisId of the diagnosis
     * @return the deleted diagnosis
     */
    @Secured({"ROLE_DOCTOR"})
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(path = "/{id}/diagnose/{d_id}")
    public DiagnoseDto deleteDiagnosis(@PathVariable("id") long id, @PathVariable("d_id") long diagnosisId) {
        LOG.trace("deleteDiagnosis({}, {})", id, diagnosisId);
        LOG.info("POST " + BASE_PATH + "/");
        DiagnoseDto dto = diagnoseService.deleteDiagnosis(id, diagnosisId);
        patientService.synchronizeWithElasticSearchDb(dto.patientId());
        return dto;
    }

    /**
     * Returns the diagnosis with the given id for the patient with the given id.
     *
     * @param id          of the patient
     * @param diagnosisId of the diagnosis
     * @return the diagnosis
     */
    @Secured({"ROLE_DOCTOR"})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}/diagnose/{d_id}")
    public DiagnoseDto viewDiagnosis(@PathVariable("id") long id, @PathVariable("d_id") long diagnosisId) {
        LOG.trace("viewDiagnosis({}, {})", id, diagnosisId);
        LOG.info("GET " + BASE_PATH + "/");
        return diagnoseService.viewDiagnosis(id, diagnosisId);
    }

    /**
     * Returns all diagnoses for the patient with the given id.
     *
     * @param id of the patient
     * @return all diagnoses
     */
    @Secured({"ROLE_DOCTOR"})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}/diagnose")
    public List<DiagnoseDto> getAllDiagnoses(@PathVariable("id") long id) {
        LOG.trace("getAllDiagnoses({})", id);
        LOG.info("GET " + BASE_PATH + "/");
        return diagnoseService.getAllDiagnoses(id);
    }

}
