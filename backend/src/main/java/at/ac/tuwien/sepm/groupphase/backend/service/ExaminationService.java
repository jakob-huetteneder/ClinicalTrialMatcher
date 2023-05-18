package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ExaminationDto;

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
}
