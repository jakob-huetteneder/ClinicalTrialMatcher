package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiagnoseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.DiagnosisMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.DiseaseMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiagnosesRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.DiagnoseService;
import at.ac.tuwien.sepm.groupphase.backend.service.DiseasesService;
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

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final DiagnosesRepository diagnosesRepository;
    private final DiagnosisMapper diagnosisMapper;
    private final DiseasesService diseasesService;
    private final DiseaseMapper diseaseMapper;

    @Autowired
    public DiagnoseServiceImpl(DiagnosesRepository diagnosesRepository, DiagnosisMapper diagnosisMapper,
                               DiseasesService diseasesService, DiseaseMapper diseaseMapper) {
        this.diagnosesRepository = diagnosesRepository;
        this.diagnosisMapper = diagnosisMapper;
        this.diseasesService = diseasesService;
        this.diseaseMapper = diseaseMapper;
    }


    @Override
    public DiagnoseDto addNewDiagnosis(DiagnoseDto diagnoseDto) {
        LOG.trace("addNewDiagnosis({})", diagnoseDto);
        LOG.debug("Add Diagnosis " + diagnoseDto + " for patient: " + diagnoseDto.patientId());
        return saveDiagnosis(diagnoseDto);
    }

    @Override
    public DiagnoseDto updateDiagnosis(DiagnoseDto diagnoseDto) {
        LOG.trace("updateDiagnosis({})", diagnoseDto);
        LOG.debug("Update Diagnosis " + diagnoseDto + " for patient: " + diagnoseDto.patientId());
        return saveDiagnosis(diagnoseDto);
    }

    /**
     * Saves a diagnosis to the database.
     *
     * @param diagnoseDto the diagnosis to save
     * @return the saved diagnosis
     */
    private DiagnoseDto saveDiagnosis(DiagnoseDto diagnoseDto) {
        LOG.trace("saveDiagnosis({})", diagnoseDto);
        Diagnose toSave = diagnosisMapper.diagnosisDtoToDiagnosis(diagnoseDto, diagnoseDto.patientId());

        toSave.setDisease(diseaseMapper.diseaseDtoToDisease(diseasesService.getPersistedDiseaseWithLink(toSave.getDisease())));

        return diagnosisMapper.diagnosisToDiagnosisDto(diagnosesRepository.save(toSave));
    }


    @Override
    public DiagnoseDto deleteDiagnosis(long id, long diagnosisId) {
        LOG.trace("deleteDiagnosis({}, {})", id, diagnosisId);
        LOG.debug("Delete Diagnosis with ID " + diagnosisId + " for patient: " + id);
        Optional<Diagnose> toDelete = diagnosesRepository.findById(diagnosisId);
        if (toDelete.isEmpty()) {
            return null;
        }
        diagnosesRepository.delete(toDelete.get());

        return diagnosisMapper.diagnosisToDiagnosisDto(toDelete.get());
    }

    @Override
    public void deleteDiagnosesByPatientId(long id) {
        LOG.trace("deleteDiagnosesByPatientId({})", id);
        LOG.debug("Delete all Diagnoses for patient: " + id);
        diagnosesRepository.deleteAllByPatient_Id(id);
    }

    @Override
    public DiagnoseDto viewDiagnosis(long id, long diagnosisId) {
        LOG.trace("viewDiagnosis({}, {})", id, diagnosisId);
        LOG.debug("View Diagnosis with ID " + diagnosisId + " for patient: " + id);
        Diagnose diagnose = diagnosesRepository.findById(diagnosisId).orElseThrow(() -> new NotFoundException("Diagnosis does not exist"));
        LOG.debug("Result: " + diagnose);
        return diagnosisMapper.diagnosisToDiagnosisDto(diagnose);
    }

    @Override
    public List<DiagnoseDto> getAllDiagnoses(long id) {
        LOG.trace("getAllDiagnoses({})", id);
        LOG.debug("View all Diagnoses for patient: " + id);
        List<Diagnose> diagnoses = diagnosesRepository.findAll();
        List<DiagnoseDto> diagnoseDtos = new ArrayList<>(diagnoses.size());
        for (Diagnose diagnose : diagnoses) {
            diagnoseDtos.add(diagnosisMapper.diagnosisToDiagnosisDto(diagnose));
        }
        return diagnoseDtos;
    }
}
