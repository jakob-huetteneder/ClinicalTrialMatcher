package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
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

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * This class defines the endpoints for the user resource.
 */
@RestController
@RequestMapping(path = UserEndpoint.BASE_URL)
public class UserEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static final String BASE_URL = "/api/v1/users";

    private final UserService userService;
    private final AuthorizationService authorizationService;
    private final Environment environment;

    public UserEndpoint(UserService userService, AuthorizationService authorizationService, Environment environment) {
        this.userService = userService;
        this.authorizationService = authorizationService;
        this.environment = environment;
    }

    /**
     * Get all users.
     *
     * @return list of all users
     */
    @Secured("ROLE_ADMIN")
    @GetMapping()
    public List<UserDetailDto> getAllUsers() {
        LOG.trace("getAllUsers()");
        LOG.info("GET " + BASE_URL + "/");
        return userService.getAllUsers();
    }

    /**
     * Update a user.
     *
     * @param id       of the user to update
     * @param toUpdate user with updated information
     * @return updated user
     */
    @Secured("ROLE_ADMIN")
    @PutMapping(path = "/{id}")
    public UserDetailDto updateUserById(@PathVariable("id") long id, @Valid @RequestBody UserUpdateDto toUpdate) {
        LOG.trace("updateUserById({}, {})", id, toUpdate);
        LOG.info("PUT " + BASE_URL + "/{}", id);
        LOG.debug("Body of request:\n{}", toUpdate);
        if (id != toUpdate.id()) {
            throw new IllegalArgumentException("ID in path and body do not match");
        }
        return userService.updateUser(toUpdate);
    }

    /**
     * Update the currently logged in user.
     *
     * @param toUpdate user with updated information
     * @return updated user
     */
    @Secured("ROLE_USER")
    @PutMapping()
    public UserDetailDto updateUser(@Valid @RequestBody UserUpdateDto toUpdate) {
        LOG.trace("updateUser({})", toUpdate);
        LOG.info("PUT " + BASE_URL + "/");
        LOG.debug("Body of request:\n{}", toUpdate);
        long id = authorizationService.getSessionUserId();
        return updateUserById(id, toUpdate.withId(id));
    }

    /**
     * Get the currently logged in user.
     *
     * @return currently logged in user
     */
    @Secured("ROLE_USER")
    @GetMapping(value = "/sessionuser")
    public UserDetailDto getActiveUser() {
        LOG.trace("getActiveUser()");
        LOG.info("GET " + BASE_URL + "/sessionuser");
        long id = authorizationService.getSessionUserId();
        return userService.getActiveUser(id);
    }

    /**
     * Delete a user.
     *
     * @param id of the user to delete
     */
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @DeleteMapping(path = "/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        LOG.trace("deleteUser({})", id);
        LOG.info("DELETE " + BASE_URL + "/{}", id);
        userService.deleteUser(id);
    }

    /**
     * Create a new user.
     *
     * @param toCreate user to create
     * @return created user
     */
    @PermitAll
    @PostMapping
    public UserDetailDto createUser(@RequestBody @Valid UserRegisterDto toCreate) {
        LOG.trace("createUser({})", toCreate);
        LOG.info("POST " + BASE_URL + "/");
        LOG.debug("Body of request: {}", toCreate);
        return userService.createUser(toCreate);
    }


    /**
     * Verify a user.
     *
     * @param code verification code
     * @param role role of the user
     * @param resp http response
     * @throws IOException if redirect fails
     */
    @GetMapping(path = "/verify")
    public void verifyUser(@Param("code") String code, @Param("role") Role role, HttpServletResponse resp) throws IOException {
        LOG.trace("verifyUser({}, {}, {})", code, role, resp);
        LOG.info("GET " + BASE_URL + "/verify?code={}&role={}", code, role);
        String frontendUrl = environment.getProperty("project.frontend.url");
        if (this.userService.verify(code, role)) {
            resp.sendRedirect(frontendUrl + "/#/account/verified");
        }
    }

    /**
     * Change the password.
     *
     * @param code verification code
     * @param pass new password
     * @return true if successful
     */
    @GetMapping(path = "/password")
    public boolean setPassword(@Param("code") String code, @Param("pass") String pass) {
        LOG.trace("setPassword({}, {})", code, pass);
        LOG.info("GET " + BASE_URL + "/password?code={}&pass=***", code);
        return userService.setPassword(pass, code);
    }

}
