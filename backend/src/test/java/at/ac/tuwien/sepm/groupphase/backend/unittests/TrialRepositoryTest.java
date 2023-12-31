package at.ac.tuwien.sepm.groupphase.backend.unittests;


import at.ac.tuwien.sepm.groupphase.backend.datagenerator.TrialDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import at.ac.tuwien.sepm.groupphase.backend.repository.TrialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test", "generateTrials", "generateUsers"})
public class TrialRepositoryTest {

    @Autowired
    private TrialRepository trialRepository;

    @Autowired
    private TrialDataGenerator trialDataGenerator;


    @BeforeEach
    public void beforeEach() {
        trialRepository.deleteAll();
        trialDataGenerator.generateTrials();
    }

    @Test
    public void testFindAll() {
        assertEquals(10, trialRepository.findAll().size());
    }


    @Test
    public void testFindByTitle() {
        Trial trial = trialDataGenerator.generateTrial();
        trialRepository.save(trial);

        assertDoesNotThrow(() -> {
            Trial persistedTrial = trialRepository.findByTitle(trial.getTitle());
            assertEquals(trial.getTitle(), persistedTrial.getTitle());
        });
    }

}
