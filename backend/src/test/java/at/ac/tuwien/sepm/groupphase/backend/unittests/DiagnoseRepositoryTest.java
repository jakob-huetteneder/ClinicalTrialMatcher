package at.ac.tuwien.sepm.groupphase.backend.unittests;


import at.ac.tuwien.sepm.groupphase.backend.datagenerator.DiagnosisDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiagnosesRepository;
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
@ActiveProfiles({"test", "generateDiagnosis", "generateDiseases"})
public class DiagnoseRepositoryTest {


    @Autowired
    private DiagnosesRepository diagnosesRepository;
    @Autowired
    private DiagnosisDataGenerator diagnosisDataGenerator;

    @BeforeEach
    public void beforeEach() {
        diagnosesRepository.deleteAll();
        diagnosisDataGenerator.generateDiagnoses();
    }

    @Test
    public void testInheritanceOfApplicationUser() {
        Diagnose diagnose = diagnosisDataGenerator.generateDiagnose();
        assertDoesNotThrow(() -> {
            Diagnose persistedDiagnose = diagnosesRepository.findById(diagnose.getId()).orElseThrow();
            assertEquals(persistedDiagnose.getId(), diagnose.getId());
            assertEquals(persistedDiagnose.getPatient().getId(), diagnose.getPatient().getId());
            assertEquals(persistedDiagnose.getDisease().getId(), diagnose.getDisease().getId());
            assertEquals(persistedDiagnose.getDate(), diagnose.getDate());
            assertEquals(persistedDiagnose.getNote(), diagnose.getNote());
        });
    }

    @Test
    public void testFindAll() {
        assertEquals(5, diagnosesRepository.findAll().size());
    }
}
