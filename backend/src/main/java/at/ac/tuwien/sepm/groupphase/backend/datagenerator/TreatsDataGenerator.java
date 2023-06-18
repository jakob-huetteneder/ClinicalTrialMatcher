package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.Treats;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.repository.TreatsRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Random;

/**
 * This class generates representative treats data.
 */
@Component
public class TreatsDataGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Faker faker = new Faker(new Random(1));
    private final TreatsRepository treatsRepository;
    private final UserDataGenerator userDataGenerator;
    private final PatientDataGenerator patientDataGenerator;


    public TreatsDataGenerator(TreatsRepository treatsRepository, UserDataGenerator userDataGenerator, PatientDataGenerator patientDataGenerator) {
        this.treatsRepository = treatsRepository;
        this.userDataGenerator = userDataGenerator;
        this.patientDataGenerator = patientDataGenerator;
    }

    /**
     * Generates a single treats relationship with fixed data.
     *
     * @param patient the patient of the treats relationship
     * @param doctor  the doctor of the treats relationship
     * @param status  the status of the treats relationship
     * @return the generated treats relationship
     */
    public Treats generateTreatsBetween(Patient patient, Doctor doctor, Treats.Status status) {
        LOG.trace("generateTreatsBetween({},{},{})", patient, doctor, status);
        return treatsRepository.save(new Treats()
            .setDoctor(doctor)
            .setPatient(patient)
            .setStatus(status));
    }

    /**
     * Generates a number of treats relationships for a patient.
     *
     * @param patient the patient of the treats relationship
     * @param count   the number of treats relationships to generate
     */
    public void generateFor(Patient patient, int count) {
        LOG.trace("generateFor({},{})", patient, count);
        for (int i = 0; i < count; i++) {
            Doctor doctor = (Doctor) userDataGenerator.generateUser(Role.DOCTOR);
            generateTreatsBetween(patient, doctor, faker.options().option(Treats.Status.class));
        }
    }

    /**
     * Generates a number of treats relationships for a doctor.
     *
     * @param doctor the doctor of the treats relationship
     * @param count  the number of treats relationships to generate
     */
    public void generateFor(Doctor doctor, int count) {
        LOG.trace("generateFor({},{})", doctor, count);
        for (int i = 0; i < count; i++) {
            Patient patient = patientDataGenerator.generatePatientWithAccount();
            generateTreatsBetween(patient, doctor, faker.options().option(Treats.Status.class));
        }
    }
}
