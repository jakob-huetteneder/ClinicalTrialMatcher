package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Admin;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository, PatientRepository patientRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtTokenizer jwtTokenizer) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenizer = jwtTokenizer;
        this.patientRepository = patientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.debug("Load all user by email");
        try {
            ApplicationUser applicationUser = findApplicationUserByEmail(email);

            List<GrantedAuthority> grantedAuthorities;
            if (applicationUser instanceof Admin) {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
            } else {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
            }

            return new User(applicationUser.getEmail(), applicationUser.getPassword(), grantedAuthorities);
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public ApplicationUser findApplicationUserByEmail(String email) {
        LOGGER.debug("Find application user by email");
        ApplicationUser applicationUser = userRepository.findByEmail(email);
        if (applicationUser != null) {
            return applicationUser;
        }
        throw new NotFoundException(String.format("Could not find the user with the email address %s", email));
    }

    @Override
    public String login(UserLoginDto userLoginDto) {
        UserDetails userDetails = loadUserByUsername(userLoginDto.getEmail());
        if (userDetails != null
            && userDetails.isAccountNonExpired()
            && userDetails.isAccountNonLocked()
            && userDetails.isCredentialsNonExpired()
            && passwordEncoder.matches(userLoginDto.getPassword(), userDetails.getPassword())
        ) {
            List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
            return jwtTokenizer.getAuthToken(userDetails.getUsername(), roles);
        }
        throw new BadCredentialsException("Username or password is incorrect or account is locked");
    }

    @Override
    public UserDetailDto updateUser(UserUpdateDto user) {
        LOGGER.debug("Update user with email {}", user.email());
        if (!userRepository.existsApplicationUserByIdAndPassword(user.id(), passwordEncoder.encode(user.oldPassword()))) {
            throw new NotFoundException("Wrong password");
        }
        ApplicationUser applicationUser = userMapper.userUpdateDtoToApplicationUser(user);
        int success = userRepository.updateUser(applicationUser.getId(), applicationUser.getFirstName(), applicationUser.getLastName(), applicationUser.getEmail(), applicationUser.getPassword(), applicationUser.getStatus().ordinal());
        if (success == 0) {
            throw new NotFoundException(String.format("Could not find the user with the email address %s", user.email()));
        }
        return userMapper.applicationUserToUserDetailDto(applicationUser);
    }

    @Override
    public List<UserDetailDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::applicationUserToUserDetailDto).toList();
    }

    @Override
    public UserDetailDto getById(long id) {
        Optional<ApplicationUser> user = userRepository.findById(id);
        if (user.isPresent()) {
            return userMapper.applicationUserToUserDetailDto(user.get());
        }
        throw new NotFoundException("User was not found");
    }

    @Override
    public void deleteUser(long id) {
        LOGGER.debug("Delete user with id {}", id);

        userRepository.deleteById(id);
    }

    @Override
    public UserDetailDto createUser(UserRegisterDto user) {
        LOGGER.debug("Create user with email {}", user.getEmail());
        ApplicationUser applicationUser = userMapper.userRegisterDtoToApplicationUser(user);
        applicationUser = userRepository.save(applicationUser);
        if (user.getRole() == Role.PATIENT) {
            Patient patient = userMapper.userRegisterDtoToPatient(user, applicationUser);
            patientRepository.save(patient);
        }
        return userMapper.applicationUserToUserDetailDto(applicationUser);
    }
}
