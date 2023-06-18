package at.ac.tuwien.sepm.groupphase.backend.util;

import at.ac.tuwien.sepm.groupphase.backend.repository.DiagnosesRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiseaseRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ExaminationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.MedicalImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TreatsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TrialRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

/**
 * Util class for database functions.
 */
@Component
public class DatabaseUtil {


    private final DiagnosesRepository diagnosesRepository;
    private final DiseaseRepository diseaseRepository;
    private final ExaminationRepository examinationRepository;
    private final MedicalImageRepository medicalImageRepository;
    private final TreatsRepository treatsRepository;
    private final PatientRepository patientRepository;
    private final TrialRepository trialRepository;
    private final UserRepository userRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public DatabaseUtil(DiagnosesRepository diagnosesRepository, DiseaseRepository diseaseRepository,
                        ExaminationRepository examinationRepository, MedicalImageRepository medicalImageRepository,
                        TreatsRepository treatsRepository, PatientRepository patientRepository,
                        TrialRepository trialRepository, UserRepository userRepository,
                        ElasticsearchOperations elasticsearchOperations) {
        this.diagnosesRepository = diagnosesRepository;
        this.diseaseRepository = diseaseRepository;
        this.examinationRepository = examinationRepository;
        this.medicalImageRepository = medicalImageRepository;
        this.treatsRepository = treatsRepository;
        this.patientRepository = patientRepository;
        this.trialRepository = trialRepository;
        this.userRepository = userRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    /**
     * Delete all entries from all tables.
     */
    @Transactional
    public void cleanAll() {
        diagnosesRepository.deleteAll(); // Must be before Disease
        diseaseRepository.deleteAll();
        medicalImageRepository.deleteAll(); // Must be before Examination
        examinationRepository.deleteAll();
        treatsRepository.deleteAll(); // Must be before Patient and User
        patientRepository.deleteAll(); // Must be before User
        trialRepository.deleteAll(); // Must be before User
        userRepository.deleteAll();
        elasticsearchOperations.deleteIndex("patients");
    }
}
