package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.Researcher;
import at.ac.tuwien.sepm.groupphase.backend.entity.Treats;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.DiseasesService;
import at.ac.tuwien.sepm.groupphase.backend.service.PatientService;
import at.ac.tuwien.sepm.groupphase.backend.util.DatabaseUtil;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Random;

/**
 * This class generates representative data for the whole application.
 */
@Component
@Profile("demo")
public class DemoDataGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final Random random = new Random(0);

    private final DatabaseUtil databaseUtil;
    private final DiagnosisDataGenerator diagnosisDataGenerator;
    private final DiseaseDataGenerator diseaseDataGenerator;
    private final ExaminationDataGenerator examinationDataGenerator;
    private final TreatsDataGenerator treatsDataGenerator;
    private final PatientDataGenerator patientDataGenerator;
    private final TrialDataGenerator trialDataGenerator;
    private final UserDataGenerator userDataGenerator;
    private final DiseasesService diseasesService;
    private final PatientRepository patientRepository;

    public DemoDataGenerator(DatabaseUtil databaseUtil, DiagnosisDataGenerator diagnosisDataGenerator,
                             DiseaseDataGenerator diseaseDataGenerator, ExaminationDataGenerator examinationDataGenerator,
                             TreatsDataGenerator treatsDataGenerator, PatientDataGenerator patientDataGenerator,
                             TrialDataGenerator trialDataGenerator, UserDataGenerator userDataGenerator,
                             DiseasesService diseasesService, PatientRepository patientRepository) {
        this.databaseUtil = databaseUtil;
        this.diagnosisDataGenerator = diagnosisDataGenerator;
        this.diseaseDataGenerator = diseaseDataGenerator;
        this.examinationDataGenerator = examinationDataGenerator;
        this.treatsDataGenerator = treatsDataGenerator;
        this.patientDataGenerator = patientDataGenerator;
        this.trialDataGenerator = trialDataGenerator;
        this.userDataGenerator = userDataGenerator;
        this.diseasesService = diseasesService;
        this.patientRepository = patientRepository;
    }

    /**
     * Generates demo data for the whole application.
     */
    @PostConstruct
    public void generateDemoData() {
        LOG.trace("generateDemoData()");
        databaseUtil.cleanAll();
        // userDataGenerator.generateUsers();
        // diseaseDataGenerator.generateDiseases();
        patientDataGenerator.generatePatientsFromJson();
        // diagnosisDataGenerator.generateDiagnoses();
        // examinationDataGenerator.generateExaminations();
        trialDataGenerator.parseTrialsFromJson();
        generateAdmin();
        // generateResearcherWithTrials();
        generateDoctorWithPatients();
        diseasesService.setAllDiseaseLinks();
    }

    /**
     * Generates a representative admin.
     */
    public void generateAdmin() {
        LOG.trace("generateAdmin()");
        userDataGenerator.generateUser(Role.ADMIN);
    }

    /**
     * Generates a representative researcher with trials.
     */
    public void generateResearcherWithTrials() {
        LOG.trace("generateResearcherWithTrials()");
        Researcher researcher = (Researcher) userDataGenerator.generateUser(Role.RESEARCHER);
        trialDataGenerator.generateTrialsFor(researcher, 15);
    }

    /**
     * Generates a representative doctor with patients.
     */
    public void generateDoctorWithPatients() {
        LOG.trace("generateDoctorWithPatients()");
        Doctor doctor = (Doctor) userDataGenerator.generateUser("Antone", "McLaughlin", "antone.mclaughlin@example.com", Role.DOCTOR, ApplicationUser.Status.ACTIVE);

        List<Patient> patients = patientRepository.findAll();
        for (Patient patient : patients) {
            Treats treats = new Treats();
            treats.setDoctor(doctor);
            treats.setPatient(patient);
            double rand = random.nextDouble();
            if (rand > 5.0/75.0) {
                treatsDataGenerator.generateTreatsBetween(patient, doctor, Treats.Status.ACCEPTED);
            } else if (rand > 3.0/75.0){
                treatsDataGenerator.generateTreatsBetween(patient, doctor, Treats.Status.REQUESTED);
            } else {
                treatsDataGenerator.generateTreatsBetween(patient, doctor, Treats.Status.DECLINED);
            }
        }
    }
}
