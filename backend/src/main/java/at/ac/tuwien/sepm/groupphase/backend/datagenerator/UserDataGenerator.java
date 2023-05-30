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
@Profile("generateData")
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
            userRepository.save(new ApplicationUser()
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setEmail(faker.internet().emailAddress())
                .setPassword(faker.internet().password())
                .setStatus(Status.ACTIVE));
        }
    }

    public ApplicationUser generateUser() {
        return new ApplicationUser()
            .setFirstName(faker.name().firstName())
            .setLastName(faker.name().lastName())
            .setEmail(faker.internet().emailAddress())
            .setPassword(faker.internet().password())
            .setStatus(Status.ACTIVE);
    }

    public ApplicationUser generateUser(Role role) {
        return userMapper.getApplicationUserFromRole(role)
            .setFirstName(faker.name().firstName())
            .setLastName(faker.name().lastName())
            .setEmail(faker.internet().emailAddress())
            .setPassword(faker.internet().password())
            .setVerification(faker.internet().macAddress())
            .setStatus(Status.ACTIVE);
    }
}
