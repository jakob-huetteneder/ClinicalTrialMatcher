package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Status;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Random;

@Component
@Profile("generateData")
public class UserDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Faker faker = new Faker(new Random(System.nanoTime()));
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDataGenerator(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @PostConstruct
    public void generateUsers() {
        LOGGER.info("Generating users");
        for (int i = 0; i < 4; i++) {
            userRepository.save(
                generateUser()
            );
        }
    }

    public ApplicationUser generateUser() {
        return generateUser(Role.PATIENT);
    }

    public ApplicationUser generateUser(Role role) {
        return userMapper.getApplicationUserFromRole(role)
            .setFirstName(faker.name().firstName())
            .setLastName(faker.name().lastName())
            .setEmail(faker.internet().emailAddress())
            // plaintext: password
            .setPassword("$2a$10$I4MVzZUBDmeiXBbuSDhWiu/867PRmxWsa4b09LHJCntT8yROgYs7S")
            .setStatus(Status.ACTIVE);
    }
}
