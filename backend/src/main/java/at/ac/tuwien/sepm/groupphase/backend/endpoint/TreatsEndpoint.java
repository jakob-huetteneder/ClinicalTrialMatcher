package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientRequestDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TreatsDto;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.TreatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Defines the endpoints for the treats resource.
 */
@RestController
@RequestMapping(path = TreatsEndpoint.BASE_PATH)
public class TreatsEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    static final String BASE_PATH = "/api/v1/treats";

    private final TreatsService treatsService;
    private final AuthorizationService authorizationService;

    public TreatsEndpoint(TreatsService treatsService, AuthorizationService authorizationService) {
        this.treatsService = treatsService;
        this.authorizationService = authorizationService;
    }

    /**
     * Returns all requests for the current user (doctor or patient).
     *
     * @param search search string for filtering
     * @return list of requests
     */
    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @GetMapping("/requests")
    public List<TreatsDto> getAllRequests(@Param("search") String search) {
        LOG.trace("getAllRequests({})", search);
        LOG.info("GET " + BASE_PATH + "/requests");
        long userId = authorizationService.getSessionUserId();
        return treatsService.getAllRequests(userId, search);
    }

    /**
     * Requests a new treats relationship between the current user (doctor) and the given patient.
     *
     * @param patientId id of the patient
     * @return the created request
     */
    @Secured("ROLE_DOCTOR")
    @PostMapping("/{patientId}")
    public PatientRequestDto requestTreats(@PathVariable("patientId") Long patientId) {
        LOG.trace("requestTreats({})", patientId);
        LOG.info("POST " + BASE_PATH + "/{}", patientId);
        long doctorId = authorizationService.getSessionUserId();
        return treatsService.requestTreats(doctorId, patientId);
    }

    /**
     * Responds to a request for a treats relationship.
     *
     * @param doctorId id of the doctor
     * @param accepted whether the request was accepted or not
     * @return the updated treats relationship
     */
    @Secured("ROLE_PATIENT")
    @PutMapping("/{doctorId}")
    public TreatsDto respondToRequest(@PathVariable("doctorId") Long doctorId, @RequestBody boolean accepted) {
        LOG.trace("respondToRequest({}, {})", doctorId, accepted);
        LOG.info("PUT " + BASE_PATH + "/{}", doctorId);
        long patientUserId = authorizationService.getSessionUserId();
        return treatsService.respondToRequest(patientUserId, doctorId, accepted);
    }

    /**
     * Deletes a treats relationship between the current user (doctor or patient) and the given user.
     *
     * @param userId id of the user
     */
    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @DeleteMapping("/{id}")
    public void deleteTreats(@PathVariable("id") Long userId) {
        LOG.trace("deleteTreats({})", userId);
        LOG.info("DELETE " + BASE_PATH + "/{}", userId);
        long sessionUserId = authorizationService.getSessionUserId();
        treatsService.deleteTreats(sessionUserId, userId);
    }
}
