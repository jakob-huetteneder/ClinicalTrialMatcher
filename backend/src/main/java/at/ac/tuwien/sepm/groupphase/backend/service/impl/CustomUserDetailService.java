package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Status;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.EmailService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
    private final AuthorizationService authorizationService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository, PatientRepository patientRepository, AuthorizationService authorizationService,
                                   UserMapper userMapper, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.userMapper = userMapper;
        this.authorizationService = authorizationService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
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
        Optional<ApplicationUser> applicationUser = userRepository.findById(user.id());

        ApplicationUser foundUser = applicationUser.orElseThrow(() -> new NotFoundException(String.format("Could not find the user with the id %d",
            user.id())));
        if (user.firstName() != null) {
            foundUser.setFirstName(user.firstName());
        }
        if (user.lastName() != null) {
            foundUser.setLastName(user.lastName());
        }
        if (user.email() != null) {
            foundUser.setEmail(user.email());
        }
        if (user.password() != null) {
            // TODO: old password check...
        }
        if (user.status() != null) {
            foundUser.setStatus(user.status());
        }
        return userMapper.applicationUserToUserDetailDto(userRepository.save(foundUser));
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
    public UserDetailDto createUser(UserRegisterDto user, String siteUrl) {
        LOGGER.debug("Create user with email {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        ApplicationUser applicationUser = userMapper.userRegisterDtoToApplicationUser(user);
        applicationUser.setVerification(passwordEncoder.encode(user.getEmail()));
        applicationUser = userRepository.save(applicationUser);
        userRepository.flush();
        if (user.getRole() == Role.PATIENT) {
            Patient patient = userMapper.userRegisterDtoToPatient(user, applicationUser);
            patientRepository.save(patient);
        }
        if (user.isCreatedByAdmin()) {
            //TODO: Send Email
        }
            this.emailService.sendVerificationEmail(applicationUser, siteUrl);
        return userMapper.applicationUserToUserDetailDto(applicationUser);
    }

    @Override
    public boolean verify(String verificationCode) {
        ApplicationUser user = userRepository.findByVerification(verificationCode);

        if (user == null || user.getStatus() == Status.ACTIVE) {
            return false;
        } else {
            user.setVerification(null);
            user.setStatus(Status.ACTIVE);
            userRepository.save(user);

            return true;
        }

    }
}
