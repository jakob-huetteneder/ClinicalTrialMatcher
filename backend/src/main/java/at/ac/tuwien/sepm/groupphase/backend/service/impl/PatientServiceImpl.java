package at.ac.tuwien.sepm.groupphase.backend.service.impl;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PatientMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class PatientServiceImpl implements PatientService {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientServiceImpl(PatientRepository trialRepository, PatientMapper patientMapper) {
        this.patientRepository = trialRepository;
        this.patientMapper = patientMapper;

    }


    @Override
    public PatientDto savePatient(PatientDto patient) {
        LOG.trace("savePatient()");
        Patient convertedTrial = patientMapper.patientDtoToPatient(patient);
        Patient savedPatient = patientRepository.save(convertedTrial);
        LOG.info("Saved patient with id='{}'", convertedTrial.getId());
        return patientMapper.patientToPatientDto(savedPatient);
    }




}
