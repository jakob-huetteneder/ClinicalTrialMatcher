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

/**
 * Util class for generating registration data.
 */
@Component
public class RegistrationDataGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Faker faker = new Faker(new Random(1));
    private final TrialRegistrationRepository trialRegistrationRepository;

    public RegistrationDataGenerator(TrialRegistrationRepository trialRegistrationRepository) {
        this.trialRegistrationRepository = trialRegistrationRepository;
    }

    /**
     * Generate registration for patient and trial
     *
     * @param patient patient to register
     * @param trial   trial to register for
     * @return registration for patient and trial
     */
    public Registration generateRegistrationBetween(Patient patient, Trial trial, Registration.Status status) {
        LOG.trace("generateRegistration({},{})", patient, trial);
        Registration registration = new Registration()
            .setPatient(patient)
            .setTrial(trial)
            .setStatus(status)
            .setDate(trial.getStartDate().minusDays(faker.random().nextInt(0, 200)));
        return trialRegistrationRepository.save(registration);
    }
}
