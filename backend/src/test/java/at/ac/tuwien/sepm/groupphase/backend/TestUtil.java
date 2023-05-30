package at.ac.tuwien.sepm.groupphase.backend;

import at.ac.tuwien.sepm.groupphase.backend.repository.DiagnosesRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiseaseRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ExaminationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.MedicalImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class TestUtil {

    @Autowired
    private DiagnosesRepository diagnosesRepository;
    @Autowired
    private DiseaseRepository diseaseRepository;
    @Autowired
    private ExaminationRepository examinationRepository;
    @Autowired
    private MedicalImageRepository medicalImageRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void cleanAll() {
        diagnosesRepository.deleteAll();
        diseaseRepository.deleteAll();
        examinationRepository.deleteAll();
        medicalImageRepository.deleteAll();
        patientRepository.deleteAll();
        userRepository.deleteAll();
    }

}
