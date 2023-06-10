package at.ac.tuwien.sepm.groupphase.backend.service.impl;


import at.ac.tuwien.sepm.groupphase.backend.elasticrepository.PatientSearchRepository;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiagnoseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ExaminationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientRequestDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PatientMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.DiagnoseService;
import at.ac.tuwien.sepm.groupphase.backend.service.ExaminationService;
import at.ac.tuwien.sepm.groupphase.backend.service.PatientService;
import at.ac.tuwien.sepm.groupphase.backend.service.TreatsService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class PatientServiceImpl implements PatientService {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final UserRepository userRepository;
    private final DiagnoseService diagnoseService;
    private final ExaminationService examinationService;
    private final TreatsService treatsService;
    private final AuthorizationService authorizationService;
    private final PatientSearchRepository patientSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public PatientServiceImpl(PatientRepository patientRepository, PatientMapper patientMapper, UserRepository userRepository,
                              DiagnoseService diagnoseService, ExaminationService examinationService, TreatsService treatsService,
                              AuthorizationService authorizationService, PatientSearchRepository patientSearchRepository,
                              ElasticsearchOperations elasticsearchOperations) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.userRepository = userRepository;
        this.diagnoseService = diagnoseService;
        this.examinationService = examinationService;
        this.treatsService = treatsService;
        this.authorizationService = authorizationService;
        this.elasticsearchOperations = elasticsearchOperations;
        this.patientSearchRepository = patientSearchRepository;
    }

    @Override
    public Stream<String> matchPatientsWithTrial(List<String> inclusion, List<String> exclusion) {
        LOG.info("Inclusion:");
        inclusion.forEach(LOG::info);
        LOG.info("Exclusion:");
        exclusion.forEach(LOG::info);
        Stream<String> patientStream = patientSearchRepository.matchPatientsWithTrial(inclusion, exclusion, PageRequest.of(0, 10));
        //if(patientStream != null)
        return patientStream;
        //return patientStream.map(patientMapper::patientToPatientDto);
        //return null;
    }

    @Override
    @Transactional
    public PatientDto savePatient(PatientDto patient) {
        LOG.trace("savePatient({})", patient);
        ApplicationUser user = userRepository.findById(authorizationService.getSessionUserId()).orElseThrow(() -> new NotFoundException("Logged in user does not exist"));
        if (!(user instanceof Doctor doctor)) {
            throw new NotFoundException("Logged in user needs to be a doctor to create a patient");
        }

        Patient convertedPatient = patientMapper.patientDtoToPatient(patient);
        convertedPatient = patientRepository.save(convertedPatient);
        elasticsearchOperations.save(convertedPatient);

        if (patient.diagnoses() != null) {
            for (DiagnoseDto diagnose : patient.diagnoses()) {
                diagnoseService.addNewDiagnosis(
                    diagnose.withPatientId(convertedPatient.getId())
                );
            }
        }
        if (patient.examinations() != null) {
            for (ExaminationDto examination : patient.examinations()) {
                examinationService.addExamination(
                    examination.withPatientId(convertedPatient.getId())
                );
            }
        }

        treatsService.doctorTreatsPatient(doctor, convertedPatient);
        LOG.info("Saved patient with id='{}'", convertedPatient.getId());
        return patientMapper.patientToPatientDto(convertedPatient);
    }

    @Override
    public PatientDto getById(long id) {
        LOG.trace("getById({})", id);
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isEmpty()) {
            //404 NOT FOUND
            LOG.warn("Patient with id {} does not exist!", id);
            throw new NotFoundException("Patient not found");
        } else {
            LOG.info("Found patient with '{}'", patientMapper.patientToPatientDto(patient.get()));
            return patientMapper.patientToPatientDto(patient.get());
        }
    }

    @Override
    public List<PatientRequestDto> getAllPatientsForDoctorId(Long doctorId, String search) {
        LOG.trace("getAllPatientsForDoctorId({})", doctorId);

        if (search == null) {
            search = "";
        }

        List<Patient> patients = patientRepository.findAllContaining(search);
        ApplicationUser user = userRepository.findById(doctorId).orElseThrow(NotFoundException::new);
        if (!(user instanceof Doctor doctor)) {
            throw new NotFoundException();
        }
        return patients.stream().filter(patient -> patient.getApplicationUser() != null).map(patient -> patientMapper.patientToPatientRequestDto(patient, doctor)).toList();
    }

    @Override
    @Transactional
    public PatientDto deleteById(long id) {
        LOG.trace("deleteById({})", id); //TODO delete patient in elastic repo
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isEmpty()) {
            //404 NOT FOUND
            LOG.warn("Patient with id {} does not exist!", id);
            return null;
        } else {
            patientRepository.deleteById(id);
            return patientMapper.patientToPatientDto(patient.get());
        }
    }
}
