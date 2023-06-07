package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.Researcher;
import at.ac.tuwien.sepm.groupphase.backend.entity.Treats;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.util.DatabaseUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("demo")
public class DemoDataGenerator {

    private final DatabaseUtil databaseUtil;
    private final DiagnosisDataGenerator diagnosisDataGenerator;
    private final DiseaseDataGenerator diseaseDataGenerator;
    private final ExaminationDataGenerator examinationDataGenerator;
    private final TreatsDataGenerator treatsDataGenerator;
    private final PatientDataGenerator patientDataGenerator;
    private final TrialDataGenerator trialDataGenerator;
    private final UserDataGenerator userDataGenerator;

    public DemoDataGenerator(DatabaseUtil databaseUtil, DiagnosisDataGenerator diagnosisDataGenerator,
                             DiseaseDataGenerator diseaseDataGenerator, ExaminationDataGenerator examinationDataGenerator,
                             TreatsDataGenerator treatsDataGenerator, PatientDataGenerator patientDataGenerator,
                             TrialDataGenerator trialDataGenerator, UserDataGenerator userDataGenerator) {
        this.databaseUtil = databaseUtil;
        this.diagnosisDataGenerator = diagnosisDataGenerator;
        this.diseaseDataGenerator = diseaseDataGenerator;
        this.examinationDataGenerator = examinationDataGenerator;
        this.treatsDataGenerator = treatsDataGenerator;
        this.patientDataGenerator = patientDataGenerator;
        this.trialDataGenerator = trialDataGenerator;
        this.userDataGenerator = userDataGenerator;
    }

    @PostConstruct
    public void generateDemoData() {
        databaseUtil.cleanAll();
        userDataGenerator.generateUsers();
        diseaseDataGenerator.generateDiseases();
        patientDataGenerator.generatePatients();
        diagnosisDataGenerator.generateDiagnoses();
        examinationDataGenerator.generateExaminations();
        trialDataGenerator.generateTrials();
        generateAdmin();
        generateResearcherWithTrials();
        generateDoctorWithPatients();
    }

    public void generateAdmin() {
        userDataGenerator.generateUser(Role.ADMIN);
    }

    public void generateResearcherWithTrials() {
        Researcher researcher = (Researcher) userDataGenerator.generateUser(Role.RESEARCHER);
        trialDataGenerator.generateTrialsFor(researcher, 15);
    }

    public void generateDoctorWithPatients() {
        Doctor doctor = (Doctor) userDataGenerator.generateUser(Role.DOCTOR);
        Patient patient = patientDataGenerator.generatePatientWithAccount();
        treatsDataGenerator.generateTreatsBetween(patient, doctor, Treats.Status.ACCEPTED);
        Patient patient2 = patientDataGenerator.generatePatientWithAccount();
        treatsDataGenerator.generateTreatsBetween(patient2, doctor, Treats.Status.REQUESTED);
        treatsDataGenerator.generateFor(patient, 10);
    }
}
