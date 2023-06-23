package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.FaqDto;
import at.ac.tuwien.sepm.groupphase.backend.service.FaqService;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

/**
 * This class defines the endpoints for the user resource.
 */
@RestController
@RequestMapping(path = FaqEndpoint.BASE_URL)
public class FaqEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static final String BASE_URL = "/api/v1/faq";

    private final FaqService faqService;

    public FaqEndpoint(FaqService faqService) {
        this.faqService = faqService;
    }


    /**
     * Get faq answer.
     *
     * @param message question
     * @param role role of the user
     */
    @PermitAll
    @GetMapping()
    public FaqDto getFaqAnswer(@Param("message") String message, @Param("role") String role) {
        LOG.trace("getFaqAnswer({}, {})", message, role);
        return this.faqService.getFaqAnswer(message, role);
    }
}
