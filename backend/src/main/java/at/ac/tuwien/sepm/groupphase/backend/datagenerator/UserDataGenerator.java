package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Status;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Profile("generateUsers")
public class UserDataGenerator {

    private static final Faker faker = new Faker(new Random(1));
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDataGenerator(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @PostConstruct
    public void generateUsers() {

        for (int i = 0; i < 4; i++) {
            generateUser();
        }
    }



    public ApplicationUser generateUser() {
        return generateUser(Role.PATIENT);
    }

    public ApplicationUser generateUser(Role role) {
        return generateUser(
            faker.name().firstName(),
            faker.name().lastName(),
            faker.internet().emailAddress(),
            "$2a$10$I4MVzZUBDmeiXBbuSDhWiu/867PRmxWsa4b09LHJCntT8yROgYs7S",
            role,
            Status.ACTIVE);
    }

    public ApplicationUser generateUser(String firstName, String lastName, String email, String password, Role role, Status status) {
        return userRepository.save(
            userMapper.getApplicationUserFromRole(role)
            .setFirstName(firstName)
            .setLastName(lastName)
            .setEmail(email)
            .setPassword(password)
            .setStatus(status));
    }
}
