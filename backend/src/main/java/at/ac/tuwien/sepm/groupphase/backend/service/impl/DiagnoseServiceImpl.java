package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiagnoseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.DiagnosisMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.DiseaseMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
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
import java.util.Optional;


@Service
public class DiagnoseServiceImpl implements DiagnoseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final DiagnosesRepository diagnosesRepository;
    private final DiagnosisMapper diagnosisMapper;
    private final DiseaseRepository diseaseRepository;
    private final DiseaseMapper diseaseMapper;


    @Autowired
    public DiagnoseServiceImpl(DiagnosesRepository diagnosesRepository, DiagnosisMapper diagnosisMapper, DiseaseRepository diseaseRepository, DiseaseMapper diseaseMapper) {
        this.diagnosesRepository = diagnosesRepository;
        this.diagnosisMapper = diagnosisMapper;
        this.diseaseRepository = diseaseRepository;
        this.diseaseMapper = diseaseMapper;
    }


    @Override
    public DiagnoseDto addNewDiagnosis(DiagnoseDto diagnoseDto) {
        LOGGER.debug("Add Diagnosis " + diagnoseDto + " for patient: " + diagnoseDto.patientId());
        return saveDiagnosis(diagnoseDto);
    }

    @Override
    public DiagnoseDto updateDiagnosis(DiagnoseDto diagnoseDto) {
        LOGGER.debug("Update Diagnosis " + diagnoseDto + " for patient: " + diagnoseDto.patientId());
        return saveDiagnosis(diagnoseDto);
    }

    private DiagnoseDto saveDiagnosis(DiagnoseDto diagnoseDto) {

        Diagnose toSave = diagnosisMapper.diagnosisDtoToDiagnosis(diagnoseDto, diagnoseDto.patientId());

        List<Disease> diseases = diseaseRepository.findDiseasesByName(diagnoseDto.disease().name());
        if (diseases.isEmpty()) {
            Disease disease = diseaseRepository.save(diseaseMapper.diseaseDtoToDisease(diagnoseDto.disease()));
            toSave.setDisease(disease);
        } else {
            toSave.setDisease(diseases.get(0));
        }

        return diagnosisMapper.diagnosisToDiagnosisDto(diagnosesRepository.save(toSave));
    }


    @Override
    public DiagnoseDto deleteDiagnosis(long id, long diagnosisId) {
        LOGGER.debug("Delete Diagnosis with ID " + diagnosisId + " for patient: " + id);
        Optional<Diagnose> toDelete = diagnosesRepository.findById(diagnosisId);
        if (toDelete.isEmpty()) {
            return null;
        }
        diagnosesRepository.delete(toDelete.get());
        return diagnosisMapper.diagnosisToDiagnosisDto(toDelete.get());
    }

    @Override
    public void deleteDiagnosesByPatientId(long id) {
        LOGGER.debug("Delete all Diagnoses for patient: " + id);
        diagnosesRepository.deleteAllByPatient_Id(id);
    }

    @Override
    public DiagnoseDto viewDiagnosis(long id, long diagnosisId) {
        LOGGER.debug("View Diagnosis with ID " + diagnosisId + " for patient: " + id);
        Diagnose diagnose = diagnosesRepository.findById(diagnosisId).orElseThrow(() -> new NotFoundException("Diagnosis does not exist"));
        LOGGER.debug("Result: " + diagnose);
        return diagnosisMapper.diagnosisToDiagnosisDto(diagnose);
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
