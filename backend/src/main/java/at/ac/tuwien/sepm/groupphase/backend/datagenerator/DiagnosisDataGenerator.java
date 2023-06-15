package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiagnosesRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.Random;

/**
 * This class generates representative diagnoses data.
 */
@Component
public class DiagnosisDataGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Faker faker = new Faker(new Random(1));
    private final DiagnosesRepository diagnosesRepository;
    private final DiseaseDataGenerator diseaseDataGenerator;
    private final PatientDataGenerator patientDataGenerator;

    private DiagnosisDataGenerator(DiagnosesRepository diagnosesRepository, DiseaseDataGenerator diseaseDataGenerator, PatientDataGenerator patientDataGenerator) {
        this.diagnosesRepository = diagnosesRepository;
        this.diseaseDataGenerator = diseaseDataGenerator;
        this.patientDataGenerator = patientDataGenerator;
    }

    /**
     * Generates diagnoses data and saves it to the database.
     */
    public void generateDiagnoses() {
        LOG.trace("generateDiagnoses()");
        for (int i = 0; i <= 4; i++) {
            generateDiagnose();
        }
    }

    /**
     * Generates a single diagnose with random (faked) data.
     *
     * @return the generated diagnose
     */
    public Diagnose generateDiagnose() {
        LOG.trace("generateDiagnose()");
        Disease disease = diseaseDataGenerator.generateDisease();
        return generateDiagnose(disease);
    }

    /**
     * Generates a single diagnose with random (faked) data. The disease is fixed.
     *
     * @param disease the disease to be diagnosed
     * @return the generated diagnose
     */
    public Diagnose generateDiagnose(Disease disease) {
        LOG.trace("generateDiagnose({})", disease);
        return generateDiagnose(
            patientDataGenerator.generatePatient(),
            disease,
            faker.date().birthday().toLocalDateTime().toLocalDate(),
            faker.funnyName().name());
    }

    /**
     * Generates a single diagnose with random (faked) data. The patient is fixed.
     *
     * @param patient the patient to be diagnosed
     * @return the generated diagnose
     */
    public Diagnose generateDiagnose(Patient patient) {
        LOG.trace("generateDiagnose({})", patient);
        return generateDiagnose(
            patient,
            diseaseDataGenerator.generateDisease(),
            faker.date().birthday().toLocalDateTime().toLocalDate(),
            faker.funnyName().name());
    }

    /**
     * Generates a single diagnose with fixed data.
     *
     * @param patient the patient to be diagnosed
     * @param disease the disease to be diagnosed
     * @param date    the date of the diagnosis
     * @param note    the note of the diagnosis
     * @return the generated diagnose
     */
    public Diagnose generateDiagnose(Patient patient, Disease disease, LocalDate date, String note) {
        LOG.trace("generateDiagnose({}, {}, {}, {})", patient, disease, date, note);
        return diagnosesRepository.save(
            new Diagnose()
                .setPatient(patient)
                .setDisease(disease)
                .setNote(note)
                .setDate(date)
        );
    }
}
