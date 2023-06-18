package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

/**
 * Defines the login endpoint.
 */
@RestController
@RequestMapping(value = "/api/v1/authentication")
public class LoginEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static final String BASE_PATH = "/api/v1/authentication";
    private final UserService userService;

    public LoginEndpoint(UserService userService) {
        this.userService = userService;
    }

    /**
     * Logs in a user.
     *
     * @param userLoginDto the user to log in
     * @return the JWT token
     */
    @PermitAll
    @PostMapping
    public String login(@RequestBody UserLoginDto userLoginDto) {
        LOG.trace("login({})", userLoginDto);
        LOG.info("POST " + BASE_PATH + "/");
        LOG.debug("Body of request: {}", userLoginDto);
        return userService.login(userLoginDto);
    }
}
