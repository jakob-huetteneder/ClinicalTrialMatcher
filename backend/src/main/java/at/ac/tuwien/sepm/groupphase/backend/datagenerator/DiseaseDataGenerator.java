package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiseaseRepository;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DiseaseDataGenerator {

    private static final Faker faker = new Faker(new Random(1));
    private final DiseaseRepository diseaseRepository;

    private DiseaseDataGenerator(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    public void generateDiseases() {
        for (int i = 0; i <= 200; i++) {
            generateDisease();
        }
    }

    public Disease generateDisease() {
        return diseaseRepository.save(
            new Disease()
            .setName(faker.disease().internalDisease()));
    }
}
