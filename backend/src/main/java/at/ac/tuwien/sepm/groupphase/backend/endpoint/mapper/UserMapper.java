package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public ApplicationUser userDetailDtoToApplicationUser(UserDetailDto userDetailDto) {
        return new ApplicationUser()
            .setId(userDetailDto.id())
            .setFirstName(userDetailDto.firstName())
            .setLastName(userDetailDto.lastName())
            .setEmail(userDetailDto.email())
            .setPassword(userDetailDto.password())
            .setStatus(userDetailDto.status());
    }

    public UserDetailDto applicationUserToUserDetailDto(ApplicationUser applicationUser) {
        return new UserDetailDto(
            applicationUser.getId(),
            applicationUser.getFirstName(),
            applicationUser.getLastName(),
            applicationUser.getEmail(),
            applicationUser.getPassword(),
            applicationUser.getStatus()
        );
    }
}
