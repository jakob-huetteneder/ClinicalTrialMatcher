package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.FaqDto;

/**
 * Service for managing users.
 */
public interface FaqService {

    /**
     * Get faq answer.
     *
     * @param message question needed to be answered
     * @param role of the user asking
     * @return answer
     */
    FaqDto getFaqAnswer(String message, String role);
}
