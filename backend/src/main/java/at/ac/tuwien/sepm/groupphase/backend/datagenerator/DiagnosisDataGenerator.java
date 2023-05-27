package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiagnosesRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiseaseRepository;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Profile("generateData")
public class DiagnosisDataGenerator {

    private static final Faker faker = new Faker(new Random(1));
    private final DiagnosesRepository diagnosesRepository;
    private final DiseaseRepository diseaseRepository;
    private final DiseaseDataGenerator diseaseDataGenerator;

    private DiagnosisDataGenerator(DiagnosesRepository diagnosesRepository, DiseaseDataGenerator diseaseDataGenerator, DiseaseRepository diseaseRepository) {
        this.diagnosesRepository = diagnosesRepository;
        this.diseaseRepository = diseaseRepository;
        this.diseaseDataGenerator = diseaseDataGenerator;
    }

    @PostConstruct
    public void generateDiagnoses() {
        for (int i = 0; i <= 4; i++) {
            Disease disease = diseaseRepository.save(diseaseDataGenerator.generateDisease());
            diagnosesRepository.save(new Diagnose()
                .setPatient(null)
                .setDisease(disease)
                .setNote(faker.funnyName().name())
                .setDate(faker.date().birthday().toLocalDateTime().toLocalDate())
            );
        }
    }

    public Diagnose generateDiagnose() {
        Disease disease = diseaseRepository.save(diseaseDataGenerator.generateDisease());
        return new Diagnose()
            .setPatient(null)
            .setDisease(disease)
            .setNote(faker.funnyName().name())
            .setDate(faker.date().birthday().toLocalDateTime().toLocalDate())
            ;
    }
}
