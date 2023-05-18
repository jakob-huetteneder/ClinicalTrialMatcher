package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ExaminationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiseaseRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.invoke.MethodHandles;

public class ExaminationMapper {
    private final PatientRepository patientRepository;
    private final DiseaseRepository diseaseRepository;

    public ExaminationMapper(PatientRepository patientRepository, DiseaseRepository diseaseRepository) {
        this.patientRepository = patientRepository;
        this.diseaseRepository = diseaseRepository;
    }

    public Examination patientExaminationDtotoExamination(ExaminationDto examinationDto) {
        Patient patient = patientRepository.getById(examinationDto.patientId());
        Disease disease = diseaseRepository.getById(examinationDto.diseaseId());
        return new Examination()
            .setPatient(patient)
            .setDisease(disease)
            .setName(examinationDto.name())
            .setDate(examinationDto.date())
            .setType(examinationDto.type())
            .setNote(examinationDto.note());
    }

    public ExaminationDto examinationtoPatientExaminationDto(Examination examination) {
        return new ExaminationDto(
            examination.getPatient().getId(),
            examination.getDisease().getId(),
            examination.getName(),
            examination.getDate(),
            examination.getType(),
            examination.getNote()
        );
    }

}
