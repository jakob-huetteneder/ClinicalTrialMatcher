package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(path = UserEndpoint.BASE_URL)
public class UserEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static final String BASE_URL = "/api/v1/users";

    private final UserService userService;

    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @PermitAll
    @GetMapping()
    public List<UserDetailDto> getAllUsers() {
        LOG.info("GET " + BASE_URL);
        return userService.getAllUsers();
    }

    @PermitAll
    @PutMapping(path = "/{id}")
    public UserDetailDto updateUser(@PathVariable("id") long id, @RequestBody UserDetailDto toUpdate) {
        LOG.info("PUT " + BASE_URL + "/{}", id);
        LOG.debug("Body of request:\n{}", toUpdate);
        if (id != toUpdate.id()) {
            // TODO: throw 400 bad request
        }
        return userService.updateUser(toUpdate);
    }

    @PermitAll
    @DeleteMapping(path = "/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        LOG.info("DELETE " + BASE_URL + "/{}", id);

        userService.deleteUser(id);
    }

}
