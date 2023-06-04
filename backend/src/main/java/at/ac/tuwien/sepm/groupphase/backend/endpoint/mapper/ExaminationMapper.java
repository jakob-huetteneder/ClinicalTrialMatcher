package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ExaminationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class ExaminationMapper {
    private final PatientRepository patientRepository;

    public ExaminationMapper(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Examination examinationDtoToExamination(ExaminationDto examinationDto, long patientId) {
        Optional<Patient> patient = patientRepository.findById(patientId);
        return new Examination()
            .setId(examinationDto.id())
            .setPatient(patient.orElseThrow(() -> new IllegalArgumentException("Patient does not exist.")))
            .setName(examinationDto.name())
            .setDate(examinationDto.date())
            .setType(examinationDto.type())
            .setNote(examinationDto.note());
    }

    public Set<Examination> examinationDtoToExamination(Set<ExaminationDto> examinations, long patientId) {
        Set<Examination> convertedExaminations = new java.util.HashSet<>();
        if (examinations != null) {
            for (ExaminationDto examination : examinations) {
                convertedExaminations.add(examinationDtoToExamination(examination, patientId));
            }
        }
        return convertedExaminations;
    }

    public ExaminationDto examinationToExaminationDto(Examination examination) {
        return new ExaminationDto(
            examination.getId(),
            examination.getPatient() != null ? examination.getPatient().getId() : null,
            examination.getName(),
            examination.getDate(),
            examination.getType(),
            examination.getNote()
        );
    }

    public Set<ExaminationDto> examinationToExaminationDto(Set<Examination> examinations) {
        Set<ExaminationDto> convertedExaminations = new java.util.HashSet<>();
        if (examinations != null) {
            for (Examination examination : examinations) {
                convertedExaminations.add(examinationToExaminationDto(examination));
            }
        }
        return convertedExaminations;
    }
}
