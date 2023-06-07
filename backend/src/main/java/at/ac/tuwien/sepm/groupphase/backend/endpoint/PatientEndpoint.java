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


@RestController
@RequestMapping(path = PatientEndpoint.BASE_PATH)
public class PatientEndpoint {

    static final String BASE_PATH = "/api/v1/patients";
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PatientServiceImpl patientService;
    private final AuthorizationService authorizationService;

    public PatientEndpoint(PatientServiceImpl patientService, AuthorizationService authorizationService) {
        this.patientService = patientService;
        this.authorizationService = authorizationService;
    }

    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public PatientDto savePatient(@RequestBody @Valid PatientDto patient) {
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

    @Secured("ROLE_DOCTOR")
    @GetMapping
    public List<PatientRequestDto> getAll(@Param("search") String search) {
        LOG.info("GET all patients");
        Long doctorId = authorizationService.getSessionUserId();
        List<PatientRequestDto> patients = patientService.getAllPatientsForDoctorId(doctorId, search);
        LOG.info("Found {} patients", patients.size());
        LOG.info("{} patients", patients);
        return patients;
    }

    @PermitAll
    @DeleteMapping("{id}")
    public PatientDto deleteById(@PathVariable long id) {
        LOG.info("DELETE patient with id {}", id);
        return patientService.deleteById(id);
    }
}
