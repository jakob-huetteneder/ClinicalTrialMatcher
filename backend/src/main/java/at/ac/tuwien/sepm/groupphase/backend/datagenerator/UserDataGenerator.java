package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Random;

/**
 * This class generates representative user data.
 */
@Component
public class UserDataGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String PASSWORD_MOCK = "$2a$10$I4MVzZUBDmeiXBbuSDhWiu/867PRmxWsa4b09LHJCntT8yROgYs7S";

    private static final Faker faker = new Faker(new Random(1));
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDataGenerator(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Generates user data and saves it to the database.
     */
    public void generateUsers() {
        LOG.trace("generateUsers()");
        for (int i = 0; i < 4; i++) {
            generateUser();
        }
    }

    /**
     * Generates a single user with random (faked) data.
     *
     * @return the generated user
     */
    public ApplicationUser generateUser() {
        LOG.trace("generateUser()");
        return generateUser(Role.PATIENT);
    }

    /**
     * Generates a single user with random (faked) data. The role is fixed.
     *
     * @param role the role of the user
     * @return the generated user
     */
    public ApplicationUser generateUser(Role role) {
        LOG.trace("generateUser({})", role);
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com";

        return generateUser(
            firstName,
            lastName,
            email,
            PASSWORD_MOCK,
            role,
            ApplicationUser.Status.ACTIVE);
    }

    /**
     * Generates a single user with fixed data.
     *
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param email     the email of the user
     * @param role      the role of the user
     * @param status    the status of the user
     * @return the generated user
     */
    public ApplicationUser generateUser(String firstName, String lastName, String email, Role role, ApplicationUser.Status status) {
        LOG.trace("generateUser({}, {}, {}, {}, {})", firstName, lastName, email, role, status);
        return generateUser(
            firstName,
            lastName,
            email,
            PASSWORD_MOCK,
            role,
            status);
    }

    /**
     * Generates a single user with fixed data including password.
     *
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param email     the email of the user
     * @param password  the password of the user
     * @param role      the role of the user
     * @param status    the status of the user
     * @return the generated user
     */
    public ApplicationUser generateUser(String firstName, String lastName, String email, String password, Role role, ApplicationUser.Status status) {
        LOG.trace("generateUser({}, {}, {}, {}, {}, {})", firstName, lastName, email, password, role, status);
        return userRepository.save(
            userMapper.getApplicationUserFromRole(role)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setPassword(password)
                .setStatus(status)
                .setVerification(faker.internet().macAddress()));
    }
}
