package at.ac.tuwien.sepm.groupphase.backend.unittests;


import at.ac.tuwien.sepm.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Status;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/*
    * This is an example of a unit test for a repository.
    * Because we use the @DataJpaTest annotation,
    * other Component Beans other than JPA Repositories
    * are not loaded by default (therefore the filter for
    * beans with the @Component annotation). With DataJpaTest,
    * every test method runs in a transaction, which is rolled
    * back after the test method is finished. This is means, that
    * the data in the database is always the same in all tests.
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Component.class))
@ActiveProfiles({"test", "generateUsers"})
public class UserRepositoryTest {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserDataGenerator userDataGenerator;

    @Test
    public void testInheritanceOfApplicationUser() {
        for (Role role : Role.values()) {
            ApplicationUser user = userDataGenerator.generateUser(role);

            ApplicationUser savedUser = userRepository.save(user);
            assertDoesNotThrow(() -> {
                ApplicationUser persistedUser = userRepository.findById(savedUser.getId()).orElseThrow();
                assertEquals(user.getFirstName(), persistedUser.getFirstName());
                assertEquals(user.getLastName(), persistedUser.getLastName());
                assertEquals(user.getEmail(), persistedUser.getEmail());
                assertEquals(user.getPassword(), persistedUser.getPassword());
                assertEquals(Status.ACTIVE, persistedUser.getStatus());

                assertEquals(role, userMapper.getRoleFromApplicationUser(persistedUser));
            });
        }
    }

    @Test
    public void testFindAll() {
        assertEquals(4, userRepository.findAll().size());
    }

    @Test
    public void testFindByEmail() {
        ApplicationUser user = userDataGenerator.generateUser();
        userRepository.save(user);

        assertDoesNotThrow(() -> {
            ApplicationUser persistedUser = userRepository.findByEmail(user.getEmail());
            assertEquals(user.getEmail(), persistedUser.getEmail());
        });
    }
}
