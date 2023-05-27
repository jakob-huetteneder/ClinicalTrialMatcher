package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ExaminationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import org.springframework.stereotype.Component;

@Component
public class ExaminationMapper {
    private final PatientRepository patientRepository;

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
