package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ExaminationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
import at.ac.tuwien.sepm.groupphase.backend.service.ExaminationService;
import jakarta.annotation.security.PermitAll;
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

@RestController
@RequestMapping(path = ExaminationEndpoint.BASE_PATH)
public class ExaminationEndpoint {

    static final String BASE_PATH = "/api/v1/patients";
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ExaminationService examinationService;


    public ExaminationEndpoint(ExaminationService examinationService) {
        this.examinationService = examinationService;
    }

    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/{id}/examination")
    public ExaminationDto addNewExamination(@PathVariable("id") long id, @RequestBody ExaminationDto examinationDto) {
        LOG.info("POST " + BASE_PATH + "/");
        LOG.debug("Body of request: {}", examinationDto);
        return examinationService.addExamination(examinationDto.withPatientId(id));
    }

    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/{id}/examination/{ex_id}")
    public ExaminationDto updateExamination(@PathVariable("id") long id, @PathVariable("ex_id") long examinationId, @RequestBody ExaminationDto examinationDto) {
        LOG.info("POST " + BASE_PATH + "/");
        LOG.debug("Body of request: {}", examinationDto);
        return examinationService.updateExamination(examinationDto.withExaminationId(examinationId).withPatientId(id));
    }

    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(path = "/{id}/examination/{ex_id}")
    public ExaminationDto deleteExamination(@PathVariable("id") long id, @PathVariable("ex_id") long examinationId) {
        LOG.info("POST " + BASE_PATH + "/");
        return examinationService.deleteExamination(id, examinationId);
    }

    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}/examination/{ex_id}")
    public ExaminationDto viewExamination(@PathVariable("id") long id, @PathVariable("ex_id") long examinationId) {
        LOG.info("GET " + BASE_PATH + "/");
        return examinationService.viewExamination(id, examinationId);
    }

    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}/examination")
    public List<ExaminationDto> getAllExaminations(@PathVariable("id") long id) {
        LOG.info("GET " + BASE_PATH + "/");
        return examinationService.getAllExaminations(id);
    }

}
