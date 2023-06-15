package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
import at.ac.tuwien.sepm.groupphase.backend.repository.ExaminationRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Random;

/**
 * This class generates representative examinations data.
 */
@Component
public class ExaminationDataGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Faker faker = new Faker(new Random(1));
    private final ExaminationRepository examinationRepository;

    private ExaminationDataGenerator(ExaminationRepository examinationRepository) {
        this.examinationRepository = examinationRepository;
    }

    /**
     * Generates examinations data and saves it to the database.
     */
    public void generateExaminations() {
        LOG.trace("generateExaminations()");
        for (int i = 0; i <= 4; i++) {
            generateExamination();
        }
    }

    /**
     * Generates a single examination with random (faked) data.
     *
     * @return the generated examination
     */
    public Examination generateExamination() {
        LOG.trace("generateExamination()");
        return examinationRepository.save(
            new Examination()
                .setPatient(null)
                .setName(faker.name().name())
                .setNote(faker.funnyName().name())
                .setType(faker.funnyName().name())
                .setDate(faker.date().birthday().toLocalDateTime().toLocalDate())
        );
    }
}
