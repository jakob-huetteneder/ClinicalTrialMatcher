package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.Researcher;
import at.ac.tuwien.sepm.groupphase.backend.entity.Treats;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.service.DiseasesService;
import at.ac.tuwien.sepm.groupphase.backend.util.DatabaseUtil;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

/**
 * This class generates representative data for the whole application.
 */
@Component
@Profile("demo")
public class DemoDataGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final DatabaseUtil databaseUtil;
    private final DiagnosisDataGenerator diagnosisDataGenerator;
    private final DiseaseDataGenerator diseaseDataGenerator;
    private final ExaminationDataGenerator examinationDataGenerator;
    private final TreatsDataGenerator treatsDataGenerator;
    private final PatientDataGenerator patientDataGenerator;
    private final TrialDataGenerator trialDataGenerator;
    private final UserDataGenerator userDataGenerator;
    private final DiseasesService diseasesService;

    public DemoDataGenerator(DatabaseUtil databaseUtil, DiagnosisDataGenerator diagnosisDataGenerator,
                             DiseaseDataGenerator diseaseDataGenerator, ExaminationDataGenerator examinationDataGenerator,
                             TreatsDataGenerator treatsDataGenerator, PatientDataGenerator patientDataGenerator,
                             TrialDataGenerator trialDataGenerator, UserDataGenerator userDataGenerator,
                             DiseasesService diseasesService) {
        this.databaseUtil = databaseUtil;
        this.diagnosisDataGenerator = diagnosisDataGenerator;
        this.diseaseDataGenerator = diseaseDataGenerator;
        this.examinationDataGenerator = examinationDataGenerator;
        this.treatsDataGenerator = treatsDataGenerator;
        this.patientDataGenerator = patientDataGenerator;
        this.trialDataGenerator = trialDataGenerator;
        this.userDataGenerator = userDataGenerator;
        this.diseasesService = diseasesService;
    }

    /**
     * Generates demo data for the whole application.
     */
    @PostConstruct
    public void generateDemoData() {
        LOG.trace("generateDemoData()");
        databaseUtil.cleanAll();
        userDataGenerator.generateUsers();
        // diseaseDataGenerator.generateDiseases();
        patientDataGenerator.generatePatientsFromJson();
        diagnosisDataGenerator.generateDiagnoses();
        examinationDataGenerator.generateExaminations();
        trialDataGenerator.parseTrialsFromJson();
        generateAdmin();
        generateResearcherWithTrials();
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
        Doctor doctor = (Doctor) userDataGenerator.generateUser(Role.DOCTOR);
        Patient patient = patientDataGenerator.generatePatientWithAccount();
        treatsDataGenerator.generateTreatsBetween(patient, doctor, Treats.Status.ACCEPTED);
        Patient patient2 = patientDataGenerator.generatePatientWithAccount();
        treatsDataGenerator.generateTreatsBetween(patient2, doctor, Treats.Status.REQUESTED);
        treatsDataGenerator.generateFor(patient, 10);
    }
}
