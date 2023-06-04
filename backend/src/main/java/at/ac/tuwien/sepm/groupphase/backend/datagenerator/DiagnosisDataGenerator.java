package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiagnosesRepository;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;

@Component
public class DiagnosisDataGenerator {

    private static final Faker faker = new Faker(new Random(1));
    private final DiagnosesRepository diagnosesRepository;
    private final DiseaseDataGenerator diseaseDataGenerator;
    private final PatientDataGenerator patientDataGenerator;

    private DiagnosisDataGenerator(DiagnosesRepository diagnosesRepository, DiseaseDataGenerator diseaseDataGenerator, PatientDataGenerator patientDataGenerator) {
        this.diagnosesRepository = diagnosesRepository;
        this.diseaseDataGenerator = diseaseDataGenerator;
        this.patientDataGenerator = patientDataGenerator;
    }

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
        return generateDiagnose(
            patientDataGenerator.generatePatient(),
            disease,
            faker.date().birthday().toLocalDateTime().toLocalDate(),
            faker.funnyName().name());
    }

    public Diagnose generateDiagnose(Patient patient) {
        return generateDiagnose(
            patient,
            diseaseDataGenerator.generateDisease(),
            faker.date().birthday().toLocalDateTime().toLocalDate(),
            faker.funnyName().name());
    }

    public Diagnose generateDiagnose(Patient patient, Disease disease, LocalDate date, String note) {
        return diagnosesRepository.save(
            new Diagnose()
            .setPatient(patient)
            .setDisease(disease)
            .setNote(note)
            .setDate(date)
        );
    }
}
