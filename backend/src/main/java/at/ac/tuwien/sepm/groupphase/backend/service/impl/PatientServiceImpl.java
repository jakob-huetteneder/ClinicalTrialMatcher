package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PatientMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiagnosesRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ExaminationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final ExaminationRepository examinationsRepository;
    private final DiagnosesRepository diagnosesRepository;

    public PatientServiceImpl(PatientRepository patientRepository, PatientMapper patientMapper,
                              ExaminationRepository examinationsRepository, DiagnosesRepository diagnosesRepository) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.examinationsRepository = examinationsRepository;
        this.diagnosesRepository = diagnosesRepository;
    }


    @Override
    public PatientDto savePatient(PatientDto patient) {
        LOG.trace("savePatient({})", patient);
        Patient convertedPatient = patientMapper.patientDtoToPatient(patient);
        convertedPatient = patientRepository.save(convertedPatient);
        List<Diagnose> convertedDiagnoses = patientMapper.patientDtoToDiagnose(patient, convertedPatient);
        List<Examination> convertedExamination = patientMapper.patientDtoToExamination(patient, convertedPatient);
        if (!convertedDiagnoses.isEmpty()) {
            diagnosesRepository.saveAll(convertedDiagnoses);
        }
        if (!convertedExamination.isEmpty()) {
            examinationsRepository.saveAll(convertedExamination);
        }
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
            throw new NotFoundException();
        } else {
            return patientMapper.patientToPatientDto(patient.get());
        }
    }

    @Override
    public PatientDto deleteById(long id) {
        LOG.trace("deleteById({})", id);
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isEmpty()) {
            //404 NOT FOUND
            LOG.warn("Patient with id {} does not exist!", id);
            throw new NotFoundException();
        } else {
            diagnosesRepository.deleteAll(patient.get().getDiagnoses());
            examinationsRepository.deleteAll(patient.get().getExaminations());
            patientRepository.deleteById(id);
            return patientMapper.patientToPatientDto(patient.get());
        }
    }
}
