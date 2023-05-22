package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ExaminationDto;
import at.ac.tuwien.sepm.groupphase.backend.service.ExaminationService;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(path = PatientEndpoint.BASE_PATH)
public class ExaminationEndpoint {

    static final String BASE_PATH = "/api/v1/patients";
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ExaminationService examinationService;


    public ExaminationEndpoint(ExaminationService examinationService) {
        this.examinationService = examinationService;
    }

    @Secured("ROLE_DOCTOR")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/{id}/examination")
    public ExaminationDto addNewExamination(@PathVariable("id") long id, @RequestBody ExaminationDto examinationDto) {
        LOG.info("POST " + BASE_PATH + "/");
        LOG.debug("Body of request: {}", examinationDto);
        return examinationService.addExamination(examinationDto.withPatientId(id));
    }

    @Secured("ROLE_DOCTOR")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/{id}/examination/{ex_id}")
    public ExaminationDto updateExamination(@PathVariable("id") long id, @PathVariable("ex_id") long examinationId, @RequestBody ExaminationDto examinationDto) {
        LOG.info("POST " + BASE_PATH + "/");
        LOG.debug("Body of request: {}", examinationDto);
        return examinationService.updateExamination(examinationDto.withExaminationId(examinationId).withPatientId(id));
    }

    @Secured("ROLE_DOCTOR")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(path = "/{id}/examination/{ex_id}")
    public ExaminationDto deleteExamination(@PathVariable("id") long id, @PathVariable("ex_id") long examinationId) {
        LOG.info("POST " + BASE_PATH + "/");
        return examinationService.deleteExamination(id, examinationId);
    }

    @Secured("ROLE_DOCTOR")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}/examination/{ex_id}")
    public ExaminationDto viewExamination(@PathVariable("id") long id, @PathVariable("ex_id") long examinationId) {
        LOG.info("POST " + BASE_PATH + "/");
        return examinationService.viewExamination(id, examinationId);
    }
}
