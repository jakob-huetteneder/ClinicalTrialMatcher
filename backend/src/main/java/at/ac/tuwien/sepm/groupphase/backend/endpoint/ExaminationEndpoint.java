package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ExaminationDto;
import at.ac.tuwien.sepm.groupphase.backend.service.ExaminationService;
import at.ac.tuwien.sepm.groupphase.backend.service.PatientService;
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
 * This class defines the endpoints for the examination resource.
 */
@RestController
@RequestMapping(path = ExaminationEndpoint.BASE_PATH)
public class ExaminationEndpoint {

    static final String BASE_PATH = "/api/v1/patients";
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ExaminationService examinationService;
    private final PatientService patientService;


    public ExaminationEndpoint(ExaminationService examinationService, PatientService patientService) {
        this.examinationService = examinationService;
        this.patientService = patientService;
    }

    /**
     * Adds a new examination to the database for the patient with the given id.
     *
     * @param id             of the patient
     * @param examinationDto to be added
     * @return the added examination
     */
    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/{id}/examination")
    public ExaminationDto addNewExamination(@PathVariable("id") long id, @RequestBody ExaminationDto examinationDto) {
        LOG.trace("addNewExamination({}, {})", id, examinationDto);
        LOG.info("POST " + BASE_PATH + "/");
        LOG.debug("Body of request: {}", examinationDto);
        ExaminationDto dto = examinationService.addExamination(examinationDto.withPatientId(id));
        patientService.synchronizeWithElasticSearchDb(id);
        return dto;
    }

    /**
     * Updates the examination with the given id.
     *
     * @param id             of the patient
     * @param examinationId  of the examination
     * @param examinationDto to be updated
     * @return the updated examination
     */
    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/{id}/examination/{ex_id}")
    public ExaminationDto updateExamination(@PathVariable("id") long id, @PathVariable("ex_id") long examinationId, @RequestBody ExaminationDto examinationDto) {
        LOG.trace("updateExamination({}, {}, {})", id, examinationId, examinationDto);
        LOG.info("POST " + BASE_PATH + "/");
        LOG.debug("Body of request: {}", examinationDto);
        ExaminationDto dto = examinationService.updateExamination(examinationDto.withExaminationId(examinationId).withPatientId(id));
        patientService.synchronizeWithElasticSearchDb(id);
        return dto;
    }

    /**
     * Deletes the examination with the given id.
     *
     * @param id            of the patient
     * @param examinationId of the examination
     * @return the deleted examination
     */
    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(path = "/{id}/examination/{ex_id}")
    public ExaminationDto deleteExamination(@PathVariable("id") long id, @PathVariable("ex_id") long examinationId) {
        LOG.trace("deleteExamination({}, {})", id, examinationId);
        LOG.info("POST " + BASE_PATH + "/");
        ExaminationDto dto = examinationService.deleteExamination(id, examinationId);
        patientService.synchronizeWithElasticSearchDb(id);
        return dto;
    }

    /**
     * Returns the examination with the given id.
     *
     * @param id            of the patient
     * @param examinationId of the examination
     * @return the examination with the given id
     */
    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}/examination/{ex_id}")
    public ExaminationDto viewExamination(@PathVariable("id") long id, @PathVariable("ex_id") long examinationId) {
        LOG.info("GET " + BASE_PATH + "/");
        return examinationService.viewExamination(id, examinationId);
    }

    /**
     * Returns all examinations of the patient with the given id.
     *
     * @param id of the patient
     * @return all examinations of the patient with the given id
     */
    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}/examination")
    public List<ExaminationDto> getAllExaminations(@PathVariable("id") long id) {
        LOG.trace("getAllExaminations({})", id);
        LOG.info("GET " + BASE_PATH + "/");
        return examinationService.getAllExaminations(id);
    }

}
