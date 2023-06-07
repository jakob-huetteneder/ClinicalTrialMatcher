package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientRequestDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TreatsDto;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.TreatsService;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = TreatsEndpoint.BASE_PATH)
public class TreatsEndpoint {

    static final String BASE_PATH = "/api/v1/treats";

    private final TreatsService treatsService;
    private final AuthorizationService authorizationService;

    public TreatsEndpoint(TreatsService treatsService, AuthorizationService authorizationService) {
        this.treatsService = treatsService;
        this.authorizationService = authorizationService;
    }

    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @GetMapping("/requests")
    public List<TreatsDto> getAllRequests(@Param("search") String search) {
        long userId = authorizationService.getSessionUserId();
        return treatsService.getAllRequests(userId, search);
    }

    @Secured("ROLE_DOCTOR")
    @PostMapping("/{patientId}")
    public PatientRequestDto requestTreats(@PathVariable("patientId") Long patientId) {
        long doctorId = authorizationService.getSessionUserId();
        return treatsService.requestTreats(doctorId, patientId);
    }

    @Secured("ROLE_PATIENT")
    @PutMapping("/{doctorId}")
    public TreatsDto respondToRequest(@PathVariable("doctorId") Long doctorId, @RequestBody boolean accepted) {
        long patientUserId = authorizationService.getSessionUserId();
        return treatsService.respondToRequest(patientUserId, doctorId, accepted);
    }

    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @DeleteMapping("/{id}")
    public void deleteTreats(@PathVariable("id") Long userId) {
        long sessionUserId = authorizationService.getSessionUserId();
        treatsService.deleteTreats(sessionUserId, userId);
    }
}
