package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Admin;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Researcher;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testUserDetailDtoToApplicationUser() {
        UserDetailDto userDetailDto = new UserDetailDto(
            1L,
            "firstName",
            "lastName",
            "email",
            Role.ADMIN,
            Status.ACTIVE
        );

        ApplicationUser mappedApplicationUser = userMapper.userDetailDtoToApplicationUser(userDetailDto);

        assertAll(
            () -> assertEquals(userDetailDto.id(), mappedApplicationUser.getId()),
            () -> assertEquals(userDetailDto.firstName(), mappedApplicationUser.getFirstName()),
            () -> assertEquals(userDetailDto.lastName(), mappedApplicationUser.getLastName()),
            () -> assertEquals(userDetailDto.email(), mappedApplicationUser.getEmail()),
            () -> assertEquals(userDetailDto.status(), mappedApplicationUser.getStatus()),
            () -> assertEquals(userDetailDto.role(), userMapper.getRoleFromApplicationUser(mappedApplicationUser)),
            () -> assertInstanceOf(Admin.class, mappedApplicationUser)
        );
    }

    @Test
    public void testApplicationUserToUserDetailDto() {

        ApplicationUser applicationUser = new Admin()
            .setId(1L)
            .setFirstName("firstName")
            .setLastName("lastName")
            .setEmail("email")
            .setPassword("password")
            .setStatus(Status.ACTIVE);

        UserDetailDto mappedUserDetailDto = userMapper.applicationUserToUserDetailDto(applicationUser);

        assertAll(
            () -> assertEquals(applicationUser.getId(), mappedUserDetailDto.id()),
            () -> assertEquals(applicationUser.getFirstName(), mappedUserDetailDto.firstName()),
            () -> assertEquals(applicationUser.getLastName(), mappedUserDetailDto.lastName()),
            () -> assertEquals(applicationUser.getEmail(), mappedUserDetailDto.email()),
            () -> assertEquals(applicationUser.getStatus(), mappedUserDetailDto.status()),
            () -> assertEquals(userMapper.getRoleFromApplicationUser(applicationUser), mappedUserDetailDto.role())
        );
    }

    @Test
    public void testGetRoleFromApplicationUser() {
        ApplicationUser admin = new Admin();
        ApplicationUser doctor = new Doctor();
        ApplicationUser researcher = new Researcher();
        ApplicationUser patient = new ApplicationUser();

        assertAll(
            () -> assertEquals(Role.ADMIN, userMapper.getRoleFromApplicationUser(admin)),
            () -> assertEquals(Role.DOCTOR, userMapper.getRoleFromApplicationUser(doctor)),
            () -> assertEquals(Role.RESEARCHER, userMapper.getRoleFromApplicationUser(researcher)),
            () -> assertEquals(Role.PATIENT, userMapper.getRoleFromApplicationUser(patient)),

            () -> assertThrows(IllegalArgumentException.class, () -> userMapper.getRoleFromApplicationUser(null))
        );
    }

    @Test
    public void testGetApplicationUserFromRole() {
        ApplicationUser admin = userMapper.getApplicationUserFromRole(Role.ADMIN);
        ApplicationUser doctor = userMapper.getApplicationUserFromRole(Role.DOCTOR);
        ApplicationUser researcher = userMapper.getApplicationUserFromRole(Role.RESEARCHER);
        ApplicationUser patient = userMapper.getApplicationUserFromRole(Role.PATIENT);

        assertAll(
            () -> assertInstanceOf(Admin.class, admin),
            () -> assertInstanceOf(Doctor.class, doctor),
            () -> assertInstanceOf(Researcher.class, researcher),
            () -> assertInstanceOf(ApplicationUser.class, patient),

            () -> assertFalse(patient instanceof Admin),
            () -> assertFalse(patient instanceof Doctor),
            () -> assertFalse(patient instanceof Researcher)
        );
    }
}
