package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialDto;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.TrialServiceImpl;
import jakarta.annotation.security.PermitAll;
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

@RestController
@RequestMapping(path = TrialEndpoint.BASE_PATH)
public class TrialEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static final String BASE_PATH = "/api/v1/trials";
    private final TrialServiceImpl trialService;

    public TrialEndpoint(TrialServiceImpl trialService) {
        this.trialService = trialService;
    }

    @Secured("ROLE_RESEARCHER")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TrialDto saveTrial(@RequestBody @Valid TrialDto trial) {
        LOG.info("Insert trial");
        LOG.info("Request Body {}", trial);

        return trialService.saveTrial(trial);
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "{id}")
    public TrialDto findTrialById(@PathVariable("id") Long id) {
        LOG.info("Get trial with id {}", id);
        return trialService.findTrialById(id);
    }

    @Secured("ROLE_RESEARCHER")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/researcher")
    public List<TrialDto> getOwnTrials() {
        LOG.info("Get own trials");
        return trialService.getOwnTrials();
    }

    @Secured("ROLE_RESEARCHER")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public List<TrialDto> getAllTrials() {
        LOG.info("Get all trials");
        return trialService.getAllTrials();
    }

    @Secured("ROLE_RESEARCHER")
    @PutMapping("{id}")
    public TrialDto update(@RequestBody @Valid TrialDto toUpdate) {
        LOG.info("PUT " + BASE_PATH + "/{}", toUpdate);
        LOG.debug("Body of request:{}", toUpdate);
        return trialService.updateTrial(toUpdate);
    }

    @Secured("ROLE_RESEARCHER")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        LOG.info("DELETE " + BASE_PATH + "/{}", id);
        LOG.debug("Body of request:{}", id);
        trialService.deleteTrialById(id);
    }

}
