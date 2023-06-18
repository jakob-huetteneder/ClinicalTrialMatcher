package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ExaminationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ExaminationMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ExaminationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.ExaminationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class ExaminationServiceImpl implements ExaminationService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ExaminationRepository examinationRepository;
    private final ExaminationMapper examinationMapper;
    private final AuthorizationService authorizationService;
    private final UserRepository userRepository;


    @Autowired
    public ExaminationServiceImpl(ExaminationRepository examinationRepository, ExaminationMapper examinationMapper, AuthorizationService authorizationService, UserRepository userRepository) {
        this.examinationRepository = examinationRepository;
        this.examinationMapper = examinationMapper;
        this.authorizationService = authorizationService;
        this.userRepository = userRepository;
    }


    @Override
    public ExaminationDto addExamination(ExaminationDto examinationDto) {
        LOG.trace("addExamination({})", examinationDto);
        LOG.debug("Add Examination Result " + examinationDto + " for patient: " + examinationDto.patientId());
        ApplicationUser loggedInUser = userRepository.findById(authorizationService.getSessionUserId())
            .orElseThrow(() -> new NotFoundException("Could not find a user for the logged in user."));
        if (!(loggedInUser instanceof Doctor)) {
            throw new NotFoundException("Could not find a doctor for the logged in user.");
        }
        Examination patientExamination = examinationRepository.save(examinationMapper.examinationDtoToExamination(examinationDto, examinationDto.patientId()));
        LOG.debug("Result: " + patientExamination);
        return examinationMapper.examinationToExaminationDto(patientExamination);
    }

    @Override
    public ExaminationDto updateExamination(ExaminationDto examinationDto) {
        LOG.trace("updateExamination({})", examinationDto);
        LOG.debug("Update Examination Result " + examinationDto + " for patient: " + examinationDto.patientId());
        Examination patientExamination = examinationRepository.save(examinationMapper.examinationDtoToExamination(examinationDto, examinationDto.patientId()));
        return examinationMapper.examinationToExaminationDto(patientExamination);
    }


    @Override
    public ExaminationDto deleteExamination(long id, long examinationId) {
        LOG.trace("deleteExamination({}, {})", id, examinationId);
        LOG.debug("Delete Examination Result with ID " + examinationId + " for patient: " + id);
        examinationRepository.delete(Objects.requireNonNull(examinationRepository.findById(examinationId).orElse(null)));
        // TODO: Remove Examination Foreign Key from Patient Table
        return viewExamination(id, examinationId);
    }

    @Override
    public void deleteExaminationsByPatientId(long id) {
        LOG.trace("deleteExaminationsByPatientId({})", id);
        LOG.debug("Delete all Examination Results for patient: " + id);
        examinationRepository.deleteAllByPatient_Id(id);
    }

    @Override
    public ExaminationDto viewExamination(long id, long examinationId) {
        LOG.trace("viewExamination({}, {})", id, examinationId);
        LOG.debug("View Examination Result with ID " + examinationId + " for patient: " + id);
        Examination patientExamination = examinationRepository.findById(examinationId).orElse(null);
        LOG.debug("Result: " + patientExamination);
        return patientExamination != null ? examinationMapper.examinationToExaminationDto(patientExamination) : null;
    }

    @Override
    public List<ExaminationDto> getAllExaminations(long id) {
        LOG.trace("getAllExaminations({})", id);
        LOG.debug("View all Examination Results for patient: " + id);
        List<Examination> patientExaminations = examinationRepository.findAll();
        List<ExaminationDto> examinationDtos = new ArrayList<>(patientExaminations.size());
        for (Examination examination : patientExaminations) {
            examinationDtos.add(examinationMapper.examinationToExaminationDto(examination));
        }
        return examinationDtos;
    }
}
