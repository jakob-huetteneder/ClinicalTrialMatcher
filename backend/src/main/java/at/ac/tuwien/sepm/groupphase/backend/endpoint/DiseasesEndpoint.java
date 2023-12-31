package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiseaseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SearchDto;
import at.ac.tuwien.sepm.groupphase.backend.service.DiseasesService;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

/**
 * This class defines the endpoints for the disease resource.
 */
@RestController
@RequestMapping(path = DiseasesEndpoint.BASE_PATH)
public class DiseasesEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static final String BASE_PATH = "/api/v1/diseases";

    private final DiseasesService service;

    public DiseasesEndpoint(DiseasesService service) {
        this.service = service;
    }

    /**
     * Finds all diseases matching the given search parameters.
     *
     * @param searchParameters the search parameters
     * @return a stream of diseases matching the search parameters
     */
    @PermitAll
    @GetMapping()
    public Stream<DiseaseDto> search(SearchDto searchParameters) {
        LOG.trace("search({})", searchParameters);
        LOG.info("GET " + BASE_PATH + " query parameters: {}", searchParameters);
        return service.search(searchParameters);
    }

}
