package at.ac.tuwien.sepm.groupphase.backend.endpoint;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ExaminationDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.ExaminationService;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping(value = "/api/v1/authentication")
public class ExaminationEndpoint {
    private final ExaminationService examinationService;

    public ExaminationEndpoint(ExaminationService examinationService) {
        this.examinationService = examinationService;
    }

    @PermitAll
    @PostMapping
    public ExaminationDto addNewExamination(@PathVariable long id, @RequestBody ExaminationDto examinationDto) {
        try {
            return examinationService.addExamination(examinationDto.withPatientId(id));
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }
}
