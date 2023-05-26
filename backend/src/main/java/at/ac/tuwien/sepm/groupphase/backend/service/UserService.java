package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {

    /**
     * Log in a user.
     *
     * @param userLoginDto login credentials
     * @return the JWT, if successful
     * @throws org.springframework.security.authentication.BadCredentialsException if credentials are bad
     */
    String login(UserLoginDto userLoginDto);

    /**
     * Update a user.
     *
     * @param user the user to update
     * @return the updated user
     */
    UserDetailDto updateUser(UserUpdateDto user);


    /**
     * Get all users.
     *
     * @return all users
     */
    List<UserDetailDto> getAllUsers();

    /**
     * Delete a user.
     *
     * @param id the id of the user to delete
     */
    void deleteUser(long id);

    /**
     * Create a user.
     *
     * @param user to Create
     * @return created user
     */
    UserDetailDto createUser(UserRegisterDto user);
}
