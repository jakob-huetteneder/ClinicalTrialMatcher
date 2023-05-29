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
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Status;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public ApplicationUser userDetailDtoToApplicationUser(UserDetailDto userDetailDto) {
        return getApplicationUserFromRole(userDetailDto.role())
            .setId(userDetailDto.id())
            .setFirstName(userDetailDto.firstName())
            .setLastName(userDetailDto.lastName())
            .setEmail(userDetailDto.email())
            .setStatus(userDetailDto.status());
    }

    public ApplicationUser userUpdateDtoToApplicationUser(UserUpdateDto userUpdateDto) {
        return getApplicationUserFromRole(userUpdateDto.role())
            .setId(userUpdateDto.id())
            .setFirstName(userUpdateDto.firstName())
            .setLastName(userUpdateDto.lastName())
            .setEmail(userUpdateDto.email())
            .setPassword(userUpdateDto.password())
            .setStatus(userUpdateDto.status());
    }

    public UserDetailDto applicationUserToUserDetailDto(ApplicationUser applicationUser) {
        return new UserDetailDto(
            applicationUser.getId(),
            applicationUser.getFirstName(),
            applicationUser.getLastName(),
            applicationUser.getEmail(),
            getRoleFromApplicationUser(applicationUser),
            applicationUser.getStatus()
        );
    }

    public ApplicationUser userRegisterDtoToApplicationUser(UserRegisterDto userRegisterDto) {
        return getApplicationUserFromRole(userRegisterDto.getRole())
            .setFirstName(userRegisterDto.getFirstName())
            .setLastName(userRegisterDto.getLastName())
            .setEmail(userRegisterDto.getEmail())
            .setPassword(userRegisterDto.getPassword())
            .setStatus(Status.ACTIVE);
    }

    public Patient userRegisterDtoToPatient(UserRegisterDto userRegisterDto, ApplicationUser applicationUser) {
        Patient patient = new Patient();
        patient.setEmail(userRegisterDto.getEmail());
        patient.setFirstName(userRegisterDto.getFirstName());
        patient.setLastName(userRegisterDto.getLastName());
        patient.setGender(userRegisterDto.getGender());
        patient.setBirthdate(userRegisterDto.getBirthdate());
        patient.setApplicationUser(applicationUser);
        return patient;
    }

    public Role getRoleFromApplicationUser(ApplicationUser applicationUser) {
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

    public ApplicationUser getApplicationUserFromRole(Role role) {
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
