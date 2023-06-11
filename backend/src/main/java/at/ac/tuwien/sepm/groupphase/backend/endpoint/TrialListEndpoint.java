package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialListDto;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.TrialListServiceImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.TrialServiceImpl;
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
@RequestMapping(path = TrialListEndpoint.BASE_PATH)
public class TrialListEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static final String BASE_PATH = "/api/v1/trialList";
    private final TrialListServiceImpl trialListService;

    public TrialListEndpoint(TrialListServiceImpl trialListService) {
        this.trialListService = trialListService;
    }



    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TrialListDto saveTrialList(@RequestBody TrialListDto trial) {
        LOG.info("Insert trialList");
        LOG.info("Request Body {}", trial);
        return trialListService.saveTrialList(trial);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public List<TrialListDto> getOwnTrialLists() {
        LOG.info("Get own trial lists");
        return trialListService.getOwnTrialLists();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    public TrialListDto getTrialListById(@PathVariable("id") Long id) {
        LOG.info("Get trial list with id {}", id);
        return trialListService.getTrialListById(id);
    }


    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}")
    public TrialListDto addTrialToTrialList(@PathVariable("id") Long id, @RequestBody TrialDto trial) {
        LOG.info("Add trial to trial list");
        LOG.info("Request Body {}", trial);
        return trialListService.addTrialToTrialList(id, trial);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}/{listId}")
    public TrialListDto deleteTrialFromTrialList(@PathVariable("id") Long id, @PathVariable("listId") Long listId) {
        LOG.info("Delete trial from trial list");
        LOG.info("Request Body {}", listId);
        return trialListService.deleteTrialFromTrialList(id, listId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public void deleteTrialList(@PathVariable("id") Long id) {
        LOG.info("Delete trial list");
        trialListService.deleteTrialList(id);
    }


}
