package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.Treats;
import at.ac.tuwien.sepm.groupphase.backend.repository.TreatsRepository;
import org.springframework.stereotype.Component;

@Component
public class TreatsDataGenerator {

    private final TreatsRepository treatsRepository;

    public TreatsDataGenerator(TreatsRepository treatsRepository) {
        this.treatsRepository = treatsRepository;
    }

    public Treats generateTreatsBetween(Patient patient, Doctor doctor, Treats.Status status) {
        return treatsRepository.save(new Treats()
            .setDoctor(doctor)
            .setPatient(patient)
            .setStatus(status));
    }
}
