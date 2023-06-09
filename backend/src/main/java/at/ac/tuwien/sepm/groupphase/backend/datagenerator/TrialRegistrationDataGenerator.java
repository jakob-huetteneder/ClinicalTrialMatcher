package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.Registration;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import at.ac.tuwien.sepm.groupphase.backend.repository.TrialRegistrationRepository;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class TrialRegistrationDataGenerator {

    private static final Faker faker = new Faker(new Random(1));
    private final TrialRegistrationRepository trialRegistrationRepository;

    private final PatientDataGenerator patientDataGenerator;
    private final TrialDataGenerator trialDataGenerator;

    public TrialRegistrationDataGenerator(TrialRegistrationRepository trialRegistrationRepository, PatientDataGenerator patientDataGenerator, TrialDataGenerator trialDataGenerator) {
        this.trialRegistrationRepository = trialRegistrationRepository;
        this.patientDataGenerator = patientDataGenerator;
        this.trialDataGenerator = trialDataGenerator;
    }

    public Registration generateTrialRegistrationBetween(Patient patient, Trial trial, Registration.Status status) {
        return trialRegistrationRepository.save(new Registration()
            .setTrial(trial)
            .setPatient(patient)
            .setStatus(status));
    }

    public void generateFor(Patient patient, int count) {
        for (int i = 0; i < count; i++) {
            Trial trial = trialDataGenerator.generateTrial();
            generateTrialRegistrationBetween(patient, trial, faker.options().option(Registration.Status.class));
        }
    }

    public void generateFor(Trial trial, int count) {
        for (int i = 0; i < count; i++) {
            Patient patient = patientDataGenerator.generatePatientWithAccount();
            generateTrialRegistrationBetween(patient, trial, faker.options().option(Registration.Status.class));
        }
    }
}
