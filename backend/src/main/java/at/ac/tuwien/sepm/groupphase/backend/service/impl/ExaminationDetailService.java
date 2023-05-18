package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ExaminationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ExaminationMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
import at.ac.tuwien.sepm.groupphase.backend.repository.ExaminationRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.ExaminationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class ExaminationDetailService implements ExaminationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ExaminationRepository examinationRepository;
    private final ExaminationMapper examinationMapper;

    @Autowired
    public ExaminationDetailService(ExaminationRepository examinationRepository, ExaminationMapper examinationMapper) {
        this.examinationRepository = examinationRepository;
        this.examinationMapper = examinationMapper;
    }

    @Override
    public ExaminationDto addExamination(ExaminationDto examinationDto) {
        LOGGER.debug("Add Examination Result for patient: " + examinationDto.patientId());
        Examination patientExamination = examinationRepository.save(examinationMapper.patientExaminationDtotoExamination(examinationDto));
        return examinationMapper.examinationtoPatientExaminationDto(patientExamination);
    }
}
