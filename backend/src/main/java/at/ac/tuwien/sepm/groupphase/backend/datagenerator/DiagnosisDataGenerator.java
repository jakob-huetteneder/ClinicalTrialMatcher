package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiagnosesRepository;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Profile("generateDiagnosis")
public class DiagnosisDataGenerator {

    private static final Faker faker = new Faker(new Random(1));
    private final DiagnosesRepository diagnosesRepository;
    private final DiseaseDataGenerator diseaseDataGenerator;

    private DiagnosisDataGenerator(DiagnosesRepository diagnosesRepository, DiseaseDataGenerator diseaseDataGenerator) {
        this.diagnosesRepository = diagnosesRepository;
        this.diseaseDataGenerator = diseaseDataGenerator;
    }

    @PostConstruct
    public void generateDiagnoses() {
        for (int i = 0; i <= 4; i++) {
            generateDiagnose();
        }
    }

    public Diagnose generateDiagnose() {
        Disease disease = diseaseDataGenerator.generateDisease();
        return generateDiagnose(disease);
    }


    public Diagnose generateDiagnose(Disease disease) {
        return diagnosesRepository.save(
            new Diagnose()
            .setPatient(null)
            .setDisease(disease)
            .setNote(faker.funnyName().name())
            .setDate(faker.date().birthday().toLocalDateTime().toLocalDate())
        );
    }
}
