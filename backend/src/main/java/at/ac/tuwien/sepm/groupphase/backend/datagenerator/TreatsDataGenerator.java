package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.Treats;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.repository.TreatsRepository;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class TreatsDataGenerator {

    private static final Faker faker = new Faker(new Random(1));
    private final TreatsRepository treatsRepository;
    private final UserDataGenerator userDataGenerator;
    private final PatientDataGenerator patientDataGenerator;


    public TreatsDataGenerator(TreatsRepository treatsRepository, UserDataGenerator userDataGenerator, PatientDataGenerator patientDataGenerator) {
        this.treatsRepository = treatsRepository;
        this.userDataGenerator = userDataGenerator;
        this.patientDataGenerator = patientDataGenerator;
    }

    public Treats generateTreatsBetween(Patient patient, Doctor doctor, Treats.Status status) {
        return treatsRepository.save(new Treats()
            .setDoctor(doctor)
            .setPatient(patient)
            .setStatus(status));
    }

    public void generateFor(Patient patient, int count) {
        for (int i = 0; i < count; i++) {
            Doctor doctor = (Doctor) userDataGenerator.generateUser(Role.DOCTOR);
            generateTreatsBetween(patient, doctor, faker.options().option(Treats.Status.class));
        }
    }

    public void generateFor(Doctor doctor, int count) {
        for (int i = 0; i < count; i++) {
            Patient patient = patientDataGenerator.generatePatientWithAccount();
            generateTreatsBetween(patient, doctor, faker.options().option(Treats.Status.class));
        }
    }
}
