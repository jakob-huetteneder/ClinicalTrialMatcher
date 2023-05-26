package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ExaminationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiseaseRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class ExaminationMapper {
    private final PatientRepository patientRepository;

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public ExaminationMapper(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Examination patientExaminationDtotoExamination(ExaminationDto examinationDto) {
        Patient patient = patientRepository.getReferenceById(examinationDto.patientId());
        return new Examination()
            .setId(examinationDto.id())
            .setPatient(patient)
            .setName(examinationDto.name())
            .setDate(examinationDto.date())
            .setType(examinationDto.type())
            .setNote(examinationDto.note());
    }

    public ExaminationDto examinationtoPatientExaminationDto(Examination examination) {
        return new ExaminationDto(
            examination.getId(),
            examination.getPatient() != null ? examination.getPatient().getId() : null,
            examination.getName(),
            examination.getDate(),
            examination.getType(),
            examination.getNote()
        );
    }

}
