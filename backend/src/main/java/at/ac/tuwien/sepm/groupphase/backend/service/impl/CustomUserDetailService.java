package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class CustomUserDetailService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final UserMapper userMapper;
    private final AuthorizationService authorizationService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository, PatientRepository patientRepository, UserMapper userMapper, AuthorizationService authorizationService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.userMapper = userMapper;
        this.authorizationService = authorizationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String login(UserLoginDto userLoginDto) {
        LOGGER.debug("Login user with email {}", userLoginDto.getEmail());
        ApplicationUser applicationUser = userRepository.findByEmail(userLoginDto.getEmail());
        if (applicationUser == null) {
            throw new UsernameNotFoundException(String.format("Could not find the user with the email address %s", userLoginDto.getEmail()));
        }
        return authorizationService.login(applicationUser, userLoginDto.getPassword());
    }

    @Override
    public UserDetailDto updateUser(UserDetailDto user) {
        LOGGER.debug("Update user with email {}", user.email());
        ApplicationUser applicationUser = userMapper.userDetailDtoToApplicationUser(user);
        int success = userRepository.updateUser(applicationUser.getId(), applicationUser.getFirstName(), applicationUser.getLastName(), applicationUser.getEmail(), applicationUser.getPassword(), applicationUser.getStatus().ordinal());
        if (success == 0) {
            throw new NotFoundException(String.format("Could not find the user with the email address %s", user.email()));
        }
        return user;
    }

    @Override
    public List<UserDetailDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::applicationUserToUserDetailDto).toList();
    }

    @Override
    public void deleteUser(long id) {
        LOGGER.debug("Delete user with id {}", id);

        userRepository.deleteById(id);
    }

    @Override
    public UserDetailDto createUser(UserRegisterDto user) {
        LOGGER.debug("Create user with email {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        ApplicationUser applicationUser = userMapper.userRegisterDtoToApplicationUser(user);
        applicationUser = userRepository.save(applicationUser);
        if (user.getRole() == Role.PATIENT) {
            Patient patient = userMapper.userRegisterDtoToPatient(user, applicationUser);
            patientRepository.save(patient);
        }
        return userMapper.applicationUserToUserDetailDto(applicationUser);
    }
}
