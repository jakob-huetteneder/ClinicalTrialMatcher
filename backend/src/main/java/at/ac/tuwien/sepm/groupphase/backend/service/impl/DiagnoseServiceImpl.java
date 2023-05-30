package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiagnoseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.DiagnosisMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.DiseaseMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiagnosesRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiseaseRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.DiagnoseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class DiagnoseServiceImpl implements DiagnoseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final DiagnosesRepository diagnosesRepository;
    private final DiagnosisMapper diagnosisMapper;
    private final DiseaseRepository diseaseRepository;
    private final DiseaseMapper diseaseMapper;
    private final AuthorizationService authorizationService;
    private final UserRepository userRepository;


    @Autowired
    public DiagnoseServiceImpl(DiagnosesRepository diagnosesRepository, DiagnosisMapper diagnosisMapper, DiseaseRepository diseaseRepository, DiseaseMapper diseaseMapper,
                               AuthorizationService authorizationService, UserRepository userRepository) {
        this.diagnosesRepository = diagnosesRepository;
        this.diagnosisMapper = diagnosisMapper;
        this.diseaseRepository = diseaseRepository;
        this.diseaseMapper = diseaseMapper;
        this.authorizationService = authorizationService;
        this.userRepository = userRepository;
    }


    @Override
    public DiagnoseDto addNewDiagnosis(DiagnoseDto diagnoseDto) {
        LOGGER.debug("Add Diagnosis " + diagnoseDto + " for patient: " + diagnoseDto.patientId());
        ApplicationUser loggedInUser = userRepository.findById(authorizationService.getSessionUserId())
            .orElseThrow(() -> new NotFoundException("Could not find a user for the logged in user."));
        if (!(loggedInUser instanceof Doctor)) {
            throw new NotFoundException("Could not find a doctor for the logged in user.");
        }
        Diagnose toSave = diagnosisMapper.diagnosisDtoToDiagnosis(diagnoseDto);
        // check if diagnose already exists
        List<Disease> diseases = diseaseRepository.findDiseasesByName(diagnoseDto.disease().name());
        if (diseases.isEmpty()) {
            Disease disease = diseaseRepository.save(diseaseMapper.diseaseDtoToDisease(diagnoseDto.disease()));
            toSave.setDisease(disease);
        } else {
            toSave.setDisease(diseases.get(0));
        }

        Diagnose diagnose = diagnosesRepository.save(toSave);
        LOGGER.debug("Result: " + diagnose);
        return diagnosisMapper.diagnosisToDiagnosisDto(diagnose);
    }

    @Override
    public DiagnoseDto updateDiagnosis(DiagnoseDto diagnoseDto) {
        LOGGER.debug("Update Diagnosis " + diagnoseDto + " for patient: " + diagnoseDto.patientId());
        Diagnose diagnose = diagnosesRepository.save(diagnosisMapper.diagnosisDtoToDiagnosis(diagnoseDto));
        return diagnosisMapper.diagnosisToDiagnosisDto(diagnose);
    }


    @Override
    public DiagnoseDto deleteDiagnosis(long id, long diagnosisId) {
        LOGGER.debug("Delete Diagnosis with ID " + diagnosisId + " for patient: " + id);
        diagnosesRepository.delete(Objects.requireNonNull(diagnosesRepository.findById(diagnosisId).orElse(null)));
        // TODO: Remove Examination Foreign Key from Patient Table
        return viewDiagnosis(id, diagnosisId);
    }

    @Override
    public DiagnoseDto viewDiagnosis(long id, long diagnosisId) {
        LOGGER.debug("View Diagnosis with ID " + diagnosisId + " for patient: " + id);
        Diagnose diagnose = diagnosesRepository.findById(diagnosisId).orElse(null);
        LOGGER.debug("Result: " + diagnose);
        return diagnose != null ? diagnosisMapper.diagnosisToDiagnosisDto(diagnose) : null;
    }

    @Override
    public List<DiagnoseDto> getAllDiagnoses(long id) {
        LOGGER.debug("View all Diagnoses for patient: " + id);
        List<Diagnose> diagnoses = diagnosesRepository.findAll();
        List<DiagnoseDto> diagnoseDtos = new ArrayList<>(diagnoses.size());
        for (Diagnose diagnose : diagnoses) {
            diagnoseDtos.add(diagnosisMapper.diagnosisToDiagnosisDto(diagnose));
        }
        return diagnoseDtos;
    }
}
