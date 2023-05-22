package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ExaminationDto;
import org.springframework.web.bind.annotation.PathVariable;

public interface ExaminationService {

    /**
     * Add Examination for a Patient.
     *
     * @param examinationDto the examination result
     * @return the created examination object
     */
    ExaminationDto addExamination(ExaminationDto examinationDto);

    /**
     * Update a certain examination result.
     *
     * @param examinationDto the examination result
     * @return the created examination object
     */
    ExaminationDto updateExamination(ExaminationDto examinationDto);

    /**
     * Delete a certain examination result.
     *
     * @param id the id of the patient
     * @param examinationId the id of the examination result
     * @return the deleted examination object
     */
    ExaminationDto deleteExamination(long id, long examinationId);

    /**
     * View a certain examination result.
     *
     * @param id the id of the patient
     * @param examinationId the id of the examination result
     * @return the viewed examination object
     */
    ExaminationDto viewExamination(long id, long examinationId);
}
