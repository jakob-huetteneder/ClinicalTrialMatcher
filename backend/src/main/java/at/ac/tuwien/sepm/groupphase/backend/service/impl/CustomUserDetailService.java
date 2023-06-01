package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Status;
import at.ac.tuwien.sepm.groupphase.backend.exception.AlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.EmailService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
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
            throw new UsernameNotFoundException("Could not find user with this email address.");
        }
        return authorizationService.login(applicationUser, userLoginDto.getPassword());
    }

    @Override
    public UserDetailDto updateUser(UserUpdateDto user) {
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
        if (user.password() != null && user.oldPassword() != null) {
            // check if old password is correct
            if (!passwordEncoder.matches(user.oldPassword(), foundUser.getPassword())) {
                throw new IllegalArgumentException("Old password is incorrect");
            }
            foundUser.setPassword(passwordEncoder.encode(user.password()));
        }
        if (user.status() != null) {
            foundUser.setStatus(user.status());
        }
        try {
            foundUser = userRepository.save(foundUser);
        } catch (DataIntegrityViolationException integrityException) {
            throw new AlreadyExistsException("User with this email already exists.", integrityException);
        }

        return userMapper.applicationUserToUserDetailDto(foundUser);
    }

    @Override
    public List<UserDetailDto> getAllUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "status")).stream().map(userMapper::applicationUserToUserDetailDto).toList();
    }

    @Override
    public UserDetailDto getActiveUser(long id) {
        Optional<ApplicationUser> applicationUser = userRepository.findById(id);

        ApplicationUser foundUser = applicationUser.orElseThrow(() -> new NotFoundException(String.format("Could not find the user with the id %d", id)));
        return userMapper.applicationUserToUserDetailDto(foundUser);
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
        applicationUser.setVerification(passwordEncoder.encode(user.getEmail()).replace("/", ""));
        try {
            applicationUser = userRepository.save(applicationUser);
        } catch (DataIntegrityViolationException integrityException) {
            throw new AlreadyExistsException("User with this email already exists.", integrityException);
        }
        if (user.getRole() == Role.PATIENT) {
            Patient patient = userMapper.userRegisterDtoToPatient(user, applicationUser);
            patientRepository.save(patient);
        }
        if (user.isCreatedByAdmin()) {
            this.emailService.sendSetPasswordEmail(applicationUser);
        } else {
            this.emailService.sendVerificationEmail(applicationUser, user.getRole());
        }
        return userMapper.applicationUserToUserDetailDto(applicationUser);
    }

    @Override
    public boolean verify(String verificationCode, Role role) {
        ApplicationUser user = userRepository.findByVerification(verificationCode);

        if (user == null || user.getStatus() == Status.ACTIVE) {
            return false;
        } else {
            user.setVerification(null);
            if (role == Role.PATIENT) {
                user.setStatus(Status.ACTIVE);
            } else {
                user.setStatus(Status.PENDING);
            }
            userRepository.save(user);

            return true;
        }

    }

    @Override
    public boolean setPassword(String pass, String verificationCode) {
        ApplicationUser applicationUser = userRepository.findByVerification(verificationCode);

        if (applicationUser == null || applicationUser.getStatus() == Status.ACTIVE) {
            return false;
        } else {
            applicationUser.setVerification(null);
            applicationUser.setStatus(Status.ACTIVE);
            applicationUser.setPassword(passwordEncoder.encode(pass));
            userRepository.save(applicationUser);

            return true;
        }

    }
}
