package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ExaminationDto;

public interface ExaminationService {

    /**
     * Find an application user based on the email address.
     *
     * @param examinationDto the examination result
     * @return a patient
     */
    ExaminationDto addExamination(ExaminationDto examinationDto);
}
