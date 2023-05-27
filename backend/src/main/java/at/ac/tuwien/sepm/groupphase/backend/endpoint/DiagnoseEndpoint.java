package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiagnoseDto;
import at.ac.tuwien.sepm.groupphase.backend.service.DiagnoseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(path = DiagnoseEndpoint.BASE_PATH)
public class DiagnoseEndpoint {

    static final String BASE_PATH = "/api/v1/patients";
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final DiagnoseService diagnoseService;


    public DiagnoseEndpoint(DiagnoseService diagnoseService) {
        this.diagnoseService = diagnoseService;
    }

    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/{id}/diagnose")
    public DiagnoseDto addNewDiagnosis(@PathVariable("id") long id, @RequestBody DiagnoseDto diagnoseDto) {
        LOG.info("POST " + BASE_PATH + "/");
        LOG.debug("Body of request: {}", diagnoseDto);
        return diagnoseService.addNewDiagnosis(diagnoseDto.withPatientId(id));
    }


    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/{id}/diagnose/{d_id}")
    public DiagnoseDto updateDiagnosis(@PathVariable("id") long id, @PathVariable("d_id") long diagnosisId, @RequestBody DiagnoseDto diagnoseDto) {
        LOG.info("POST " + BASE_PATH + "/");
        LOG.debug("Body of request: {}", diagnoseDto);
        return diagnoseService.updateDiagnosis(diagnoseDto.withDiagnosisId(diagnosisId).withPatientId(id));
    }

    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(path = "/{id}/diagnose/{d_id}")
    public DiagnoseDto deleteDiagnosis(@PathVariable("id") long id, @PathVariable("d_id") long diagnosisId) {
        LOG.info("POST " + BASE_PATH + "/");
        return diagnoseService.deleteDiagnosis(id, diagnosisId);
    }

    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}/diagnose/{d_id}")
    public DiagnoseDto viewDiagnosis(@PathVariable("id") long id, @PathVariable("d_id") long diagnosisId) {
        LOG.info("GET " + BASE_PATH + "/");
        return diagnoseService.viewDiagnosis(id, diagnosisId);
    }

    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}/diagnose")
    public List<DiagnoseDto> getAllDiagnoses(@PathVariable("id") long id) {
        LOG.info("GET " + BASE_PATH + "/");
        return diagnoseService.getAllDiagnoses(id);
    }

}
