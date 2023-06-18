package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiseaseRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Random;

/**
 * This class generates representative diseases data.
 */
@Component
public class DiseaseDataGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Faker faker = new Faker(new Random(1));
    private final DiseaseRepository diseaseRepository;

    private DiseaseDataGenerator(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    /**
     * Generates diseases data and saves it to the database.
     */
    public void generateDiseases() {
        LOG.trace("generateDiseases()");
        for (int i = 0; i <= 200; i++) {
            generateDisease();
        }
    }

    /**
     * Generates a single disease with random (faked) data.
     *
     * @return the generated disease
     */
    public Disease generateDisease() {
        LOG.trace("generateDisease()");
        return diseaseRepository.save(
            new Disease()
                .setName(faker.disease().internalDisease()));
    }
}
