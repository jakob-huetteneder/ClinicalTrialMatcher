package at.ac.tuwien.sepm.groupphase.backend.unittests;


import at.ac.tuwien.sepm.groupphase.backend.datagenerator.ExaminationDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
import at.ac.tuwien.sepm.groupphase.backend.repository.ExaminationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
@SpringBootTest
@ActiveProfiles({"test", "generateExaminations"})
public class ExaminationRepositoryTest {


    @Autowired
    private ExaminationRepository examinationRepository;
    @Autowired
    private ExaminationDataGenerator examinationDataGenerator;

    @BeforeEach
    public void beforeEach() {
        examinationRepository.deleteAll();
        examinationDataGenerator.generateExaminations();
    }

    @Test
    public void testInheritanceOfApplicationUser() {
        Examination examination = examinationDataGenerator.generateExamination();

        assertDoesNotThrow(() -> {
            Examination persistedExamination = examinationRepository.findById(examination.getId()).orElseThrow();
            assertEquals(persistedExamination.getId(), examination.getId());
            assertEquals(persistedExamination.getPatient(), examination.getPatient());
            assertEquals(persistedExamination.getName(), examination.getName());
            assertEquals(persistedExamination.getDate(), examination.getDate());
            assertEquals(persistedExamination.getType(), examination.getType());
            assertEquals(persistedExamination.getNote(), examination.getNote());
        });
    }

    @Test
    public void testFindAll() {
        assertEquals(5, examinationRepository.findAll().size());
    }
}
