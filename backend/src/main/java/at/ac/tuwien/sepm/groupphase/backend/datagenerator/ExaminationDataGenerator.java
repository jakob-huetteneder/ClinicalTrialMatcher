package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
import at.ac.tuwien.sepm.groupphase.backend.repository.ExaminationRepository;

import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Profile("generateExaminations")
public class ExaminationDataGenerator {

    private static final Faker faker = new Faker(new Random(1));
    private final ExaminationRepository examinationRepository;

    private ExaminationDataGenerator(ExaminationRepository examinationRepository) {
        this.examinationRepository = examinationRepository;
    }

    @PostConstruct
    public void generateExaminations() {
        for (int i = 0; i <= 4; i++) {
            generateExamination();
        }
    }

    public Examination generateExamination() {
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
