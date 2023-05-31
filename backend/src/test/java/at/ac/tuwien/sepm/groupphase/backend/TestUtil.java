package at.ac.tuwien.sepm.groupphase.backend;

import at.ac.tuwien.sepm.groupphase.backend.repository.*;
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
    private TrialRepository trialRepository;
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void cleanAll() {
        diagnosesRepository.deleteAll(); // Must be before Disease
        diseaseRepository.deleteAll();
        medicalImageRepository.deleteAll(); // Must be before Examination
        examinationRepository.deleteAll();
        patientRepository.deleteAll(); // Must be before User
        trialRepository.deleteAll(); // Must be before User
        userRepository.deleteAll();
    }

}
