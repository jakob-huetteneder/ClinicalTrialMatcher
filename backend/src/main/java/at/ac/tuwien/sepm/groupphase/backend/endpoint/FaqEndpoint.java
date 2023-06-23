package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.FaqDto;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.FaqService;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
    private final AuthorizationService authorizationService;
    private final Environment environment;

    public FaqEndpoint(FaqService faqService, AuthorizationService authorizationService, Environment environment) {
        this.faqService = faqService;
        this.authorizationService = authorizationService;
        this.environment = environment;
    }


    /**
     * Verify a user.
     *
     * @param message verification code
     * @throws IOException if redirect fails
     */
    @PermitAll
    @GetMapping() //headers = "Accept=*/*", produces = MediaType.TEXT_PLAIN_VALUE
    public FaqDto getFaqAnswer(@Param("message") String message, @Param("role") String role) {
        LOG.trace("getFaqAnswer({})", message, role);
        return this.faqService.getFaqAnswer(message, role);
    }

    @PermitAll
    @GetMapping(path = "/test")
    public boolean getFaqAwnser1(@Param("message") String message) throws IOException {
        return true;
    }
}
