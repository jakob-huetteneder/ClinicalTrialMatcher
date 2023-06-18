package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.Registration;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import at.ac.tuwien.sepm.groupphase.backend.repository.TrialRegistrationRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Random;

@Component
public class TrialRegistrationDataGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Faker faker = new Faker(new Random(1));
    private final TrialRegistrationRepository trialRegistrationRepository;

    private final PatientDataGenerator patientDataGenerator;
    private final TrialDataGenerator trialDataGenerator;

    public TrialRegistrationDataGenerator(TrialRegistrationRepository trialRegistrationRepository, PatientDataGenerator patientDataGenerator, TrialDataGenerator trialDataGenerator) {
        this.trialRegistrationRepository = trialRegistrationRepository;
        this.patientDataGenerator = patientDataGenerator;
        this.trialDataGenerator = trialDataGenerator;
    }

    /**
     * Generates trial registration data and saves it to the database.
     *
     * @param patient the patient to register
     * @param trial   the trial to register for
     * @param status  the status of the registration
     * @return the generated registration
     */
    public Registration generateTrialRegistrationBetween(Patient patient, Trial trial, Registration.Status status) {
        LOG.trace("generateTrialRegistrationBetween({}, {}, {})", patient, trial, status);
        return trialRegistrationRepository.save(new Registration()
            .setTrial(trial)
            .setPatient(patient)
            .setStatus(status));
    }

    /**
     * Generates trial registration data for a specific patient.
     *
     * @param patient the patient to register
     * @param count   the number of registrations to generate
     */
    public void generateFor(Patient patient, int count) {
        for (int i = 0; i < count; i++) {
            Trial trial = trialDataGenerator.generateTrial();
            generateTrialRegistrationBetween(patient, trial, faker.options().option(Registration.Status.class));
        }
    }

    /**
     * Generates trial registration data for a specific trial.
     *
     * @param trial the trial to register for
     * @param count the number of registrations to generate
     */
    public void generateFor(Trial trial, int count) {
        for (int i = 0; i < count; i++) {
            Patient patient = patientDataGenerator.generatePatientWithAccount();
            generateTrialRegistrationBetween(patient, trial, faker.options().option(Registration.Status.class));
        }
    }
}
