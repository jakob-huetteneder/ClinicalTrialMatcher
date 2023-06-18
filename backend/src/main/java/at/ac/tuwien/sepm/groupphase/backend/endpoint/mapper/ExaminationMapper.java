package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ExaminationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.Set;

/**
 * Mapper for mapping {@link Examination} to {@link ExaminationDto} and vice versa.
 */
@Component
public class ExaminationMapper {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PatientRepository patientRepository;

    public ExaminationMapper(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Converts an examinationDTO to an examination entity.
     *
     * @param examinationDto the examination DTO to be converted
     * @param patientId      the ID of the patient to be set
     * @return the converted examination entity
     */
    public Examination examinationDtoToExamination(ExaminationDto examinationDto, Long patientId) {
        LOG.trace("examinationDtoToExamination({}, {})", examinationDto, patientId);
        Optional<Patient> patient = patientId != null ? patientRepository.findById(patientId) : Optional.empty();
        return new Examination()
            .setId(examinationDto.id())
            .setPatient(patient.isPresent() ? patient.orElseThrow(() -> new IllegalArgumentException("Patient does not exist.")) : null)
            .setName(examinationDto.name())
            .setDate(examinationDto.date())
            .setType(examinationDto.type())
            .setNote(examinationDto.note());
    }

    /**
     * Converts a set of examination DTOs to a set of examination entities.
     *
     * @param examinations the set of examination DTOs to be converted
     * @param patientId    the ID of the patient to be set
     * @return the converted set of examination entities
     */
    public Set<Examination> examinationDtoToExamination(Set<ExaminationDto> examinations, Long patientId) {
        LOG.trace("examinationDtoToExamination({}, {})", examinations, patientId);
        Set<Examination> convertedExaminations = new java.util.HashSet<>();
        if (examinations != null) {
            for (ExaminationDto examination : examinations) {
                convertedExaminations.add(examinationDtoToExamination(examination, patientId));
            }
        }
        return convertedExaminations;
    }

    /**
     * Converts an examination entity to an examination DTO.
     *
     * @param examination the examination entity to be converted
     * @return the converted examination DTO
     */
    public ExaminationDto examinationToExaminationDto(Examination examination) {
        LOG.trace("examinationToExaminationDto({})", examination);
        return new ExaminationDto(
            examination.getId(),
            examination.getPatient() != null ? examination.getPatient().getId() : null,
            examination.getName(),
            examination.getDate(),
            examination.getType(),
            examination.getNote()
        );
    }

    /**
     * Converts a set of examination entities to a set of examination DTOs.
     *
     * @param examinations the set of examination entities to be converted
     * @return the converted set of examination DTOs
     */
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
