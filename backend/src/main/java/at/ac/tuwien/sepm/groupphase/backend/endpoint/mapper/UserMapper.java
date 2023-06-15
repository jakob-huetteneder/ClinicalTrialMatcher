package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Admin;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.Researcher;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Set;

/**
 * Mapper for mapping {@link ApplicationUser} to {@link UserDetailDto} and vice versa.
 */
@Component
public class UserMapper {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Maps the given userDetailDto to an applicationUser.
     *
     * @param userDetailDto to be mapped
     * @return the mapped applicationUser
     */
    public ApplicationUser userDetailDtoToApplicationUser(UserDetailDto userDetailDto) {
        LOG.trace("userDetailDtoToApplicationUser({})", userDetailDto);
        return getApplicationUserFromRole(userDetailDto.role())
            .setId(userDetailDto.id())
            .setFirstName(userDetailDto.firstName())
            .setLastName(userDetailDto.lastName())
            .setEmail(userDetailDto.email())
            .setStatus(userDetailDto.status());
    }

    /**
     * Maps the given set of userDetailDto to a set of applicationUser.
     *
     * @param userDetailDtos to be mapped
     * @return the mapped applicationUsers
     */
    public Set<ApplicationUser> userDetailDtoToApplicationUser(Set<UserDetailDto> userDetailDtos) {
        LOG.trace("userDetailDtoToApplicationUser({})", userDetailDtos);
        Set<ApplicationUser> applicationUsers = new HashSet<>();
        if (userDetailDtos != null) {
            for (UserDetailDto userDetailDto1 : userDetailDtos) {
                applicationUsers.add(userDetailDtoToApplicationUser(userDetailDto1));
            }
        }
        return applicationUsers;
    }

    /**
     * Maps the given userUpdateDto to an applicationUser.
     *
     * @param userUpdateDto to be mapped
     * @return the mapped applicationUser
     */
    public ApplicationUser userUpdateDtoToApplicationUser(UserUpdateDto userUpdateDto) {
        LOG.trace("userUpdateDtoToApplicationUser({})", userUpdateDto);
        return getApplicationUserFromRole(userUpdateDto.role())
            .setId(userUpdateDto.id())
            .setFirstName(userUpdateDto.firstName())
            .setLastName(userUpdateDto.lastName())
            .setEmail(userUpdateDto.email())
            .setPassword(userUpdateDto.password())
            .setStatus(userUpdateDto.status());
    }

    /**
     * Maps the given applicationUser to a userDetailDto.
     *
     * @param applicationUser to be mapped
     * @return the mapped userDetailDto
     */
    public UserDetailDto applicationUserToUserDetailDto(ApplicationUser applicationUser) {
        LOG.trace("applicationUserToUserDetailDto({})", applicationUser);
        return new UserDetailDto(
            applicationUser.getId(),
            applicationUser.getFirstName(),
            applicationUser.getLastName(),
            applicationUser.getEmail(),
            getRoleFromApplicationUser(applicationUser),
            applicationUser.getStatus()
        );
    }

    /**
     * Maps the given set of applicationUser to a set of userDetailDto.
     *
     * @param applicationUsers to be mapped
     * @return the mapped userDetailDtos
     */
    public Set<UserDetailDto> applicationUserToUserDetailDto(Set<? extends ApplicationUser> applicationUsers) {
        LOG.trace("applicationUserToUserDetailDto({})", applicationUsers);
        Set<UserDetailDto> userDetailDtos = new HashSet<>();
        if (applicationUsers != null) {
            for (ApplicationUser applicationUser : applicationUsers) {
                userDetailDtos.add(applicationUserToUserDetailDto(applicationUser));
            }
        }
        return userDetailDtos;
    }

    /**
     * Maps the given userRegisterDto to an applicationUser.
     *
     * @param userRegisterDto to be mapped
     * @return the mapped applicationUser
     */
    public ApplicationUser userRegisterDtoToApplicationUser(UserRegisterDto userRegisterDto) {
        LOG.trace("userRegisterDtoToApplicationUser({})", userRegisterDto);
        return getApplicationUserFromRole(userRegisterDto.getRole())
            .setFirstName(userRegisterDto.getFirstName())
            .setLastName(userRegisterDto.getLastName())
            .setEmail(userRegisterDto.getEmail())
            .setPassword(userRegisterDto.getPassword())
            .setStatus(ApplicationUser.Status.ACTION_REQUIRED);
    }

    /**
     * Maps the given userRegisterDto and applicationUser to a patient.
     *
     * @param userRegisterDto to be mapped
     * @param applicationUser to be mapped
     * @return the mapped patient
     */
    public Patient userRegisterDtoToPatient(UserRegisterDto userRegisterDto, ApplicationUser applicationUser) {
        LOG.trace("userRegisterDtoToPatient({}, {})", userRegisterDto, applicationUser);
        Patient patient = new Patient();
        patient.setEmail(userRegisterDto.getEmail());
        patient.setFirstName(userRegisterDto.getFirstName());
        patient.setLastName(userRegisterDto.getLastName());
        patient.setGender(userRegisterDto.getGender());
        patient.setBirthdate(userRegisterDto.getBirthdate());
        patient.setApplicationUser(applicationUser);
        patient.setVerification(applicationUser.getVerification());
        return patient;
    }

    /**
     * Finds the role of the given applicationUser.
     *
     * @param applicationUser to be analyzed
     * @return the role of the given applicationUser
     */
    public Role getRoleFromApplicationUser(ApplicationUser applicationUser) {
        LOG.trace("getRoleFromApplicationUser({})", applicationUser);
        if (applicationUser == null) {
            throw new IllegalArgumentException("Cannot get role of applicationUser = null");
        }

        if (applicationUser instanceof Admin) {
            return Role.ADMIN;
        } else if (applicationUser instanceof Doctor) {
            return Role.DOCTOR;
        } else if (applicationUser instanceof Researcher) {
            return Role.RESEARCHER;
        } else {
            return Role.PATIENT;
        }
    }

    /**
     * Creates a new instance of an applicationUser with the correct role
     *
     * @param role of the applicationUser
     * @return the new instance of the applicationUser
     */
    public ApplicationUser getApplicationUserFromRole(Role role) {
        LOG.trace("getApplicationUserFromRole({})", role);
        switch (role) {
            case ADMIN -> {
                return new Admin();
            }
            case DOCTOR -> {
                return new Doctor();
            }
            case RESEARCHER -> {
                return new Researcher();
            }
            default -> {
                return new ApplicationUser();
            }
        }
    }
}
