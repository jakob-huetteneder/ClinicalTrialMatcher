package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiseaseDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;

import java.util.Set;

/**
 * Service for analyzing admission notes.
 */
public interface AdmissionNoteAnalyzerService {

    /**
     * Analyzes the admission note and returns a list of diseases.
     *
     * @param admissionNote the admission note
     * @return a set of diseases
     */
    Set<DiseaseDto> extractDiseases(String admissionNote);

    /**
     * Analyzes the admission note and returns the age of the patient.
     *
     * @param admissionNote the admission note
     * @return the age of the patient
     */
    Integer extractAge(String admissionNote);

    /**
     * Analyzes the admission note and returns the gender of the patient.
     *
     * @param admissionNote the admission note
     * @return the gender of the patient
     */
    Gender extractGender(String admissionNote);
}
