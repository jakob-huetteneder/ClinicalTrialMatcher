package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientDto;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.PatientServiceImpl;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;


@RestController
@RequestMapping(path = PatientEndpoint.BASE_PATH)
public class PatientEndpoint {

    static final String BASE_PATH = "/api/v1/patients";
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PatientServiceImpl patientService;

    public PatientEndpoint(PatientServiceImpl patientService) {
        this.patientService = patientService;
    }

    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public PatientDto savePatient(@RequestBody PatientDto patient) {
        LOG.info("Insert patient");
        LOG.info("request Body ({},{})", patient.firstName(), patient.lastName());
        return patientService.savePatient(patient);
    }

    @PermitAll
    @GetMapping("{id}")
    public PatientDto getById(@PathVariable long id) {
        LOG.info("GET patient with id {}", id);
        return patientService.getById(id);
    }

    @PermitAll
    @DeleteMapping("{id}")
    public PatientDto deleteById(@PathVariable long id) {
        LOG.info("DELETE patient with id {}", id);
        return patientService.deleteById(id);
    }
}
