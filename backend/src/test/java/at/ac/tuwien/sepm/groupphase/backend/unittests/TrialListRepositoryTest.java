package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.datagenerator.TrialListDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.entity.TrialList;
import at.ac.tuwien.sepm.groupphase.backend.repository.TrialListRepository;
import org.junit.jupiter.api.BeforeEach;
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

@ExtendWith(SpringExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Component.class))
@ActiveProfiles({"test", "generateTrialLists", "generateUsers"})
public class TrialListRepositoryTest {

    @Autowired
    private TrialListRepository trialListRepository;

    @Autowired
    private TrialListDataGenerator trialListDataGenerator;

    @BeforeEach
    public void beforeEach() {
        trialListRepository.deleteAll();
        trialListDataGenerator.generateTrialLists();
    }

    @Test
    public void testFindAll() {
        assertEquals(10, trialListRepository.findAll().size());
    }

    @Test
    public void testFindByName() {
        TrialList trialList = trialListDataGenerator.generateTrialList();
        trialListRepository.save(trialList);

        assertDoesNotThrow(() -> {
            TrialList persistedTrial = trialListRepository.findByName(trialList.getName());
            assertEquals(trialList.getName(), persistedTrial.getName());
        });
    }
}
