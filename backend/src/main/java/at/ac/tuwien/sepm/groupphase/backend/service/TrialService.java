package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;

import java.util.List;

public interface TrialService {

    /**
     * Find and return all trials in the database.
     *
     * @return all trials
     */
    List<Trial> getAllTrials();

    /**
     * Find and return the trial with the given id.
     *
     * @param id of the trial to find
     * @return the trial with the given id
     */
    Trial findTrialById(Long id);

    List<Trial> getOwnTrials();

    /**
     * Save a new trial given by the trialDto.
     *
     * @param trial the dto
     * @return the saved trial as a dto
     */
    TrialDto saveTrial(TrialDto trial);

    /**
     * Update a trial given by the trialDto.
     *
     * @param trial the dto
     * @return the updated trial as a dto
     */
    TrialDto updateTrial(TrialDto trial);

    /**
     * Delete a trial given by the id.
     *
     * @param id the id of the trial to delete
     */
    void deleteTrialById(Long id);

}
