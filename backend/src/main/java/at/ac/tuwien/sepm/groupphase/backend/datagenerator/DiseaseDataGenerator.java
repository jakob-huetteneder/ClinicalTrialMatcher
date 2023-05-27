package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiseaseRepository;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Profile("generateData")
public class DiseaseDataGenerator {

    private static final Faker faker = new Faker(new Random(1));
    private final DiseaseRepository diseaseRepository;

    private DiseaseDataGenerator(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    @PostConstruct
    public void generateDiseases() {
        for (int i = 0; i <= 4; i++) {
            diseaseRepository.save(new Disease()
                .setName(faker.disease().internalDisease())
                .setSynonyms(faker.disease().internalDisease())
            );
        }
    }

    public Disease generateDisease() {
        return new Disease()
            .setName(faker.disease().internalDisease())
            .setSynonyms(faker.disease().internalDisease());
    }
}
