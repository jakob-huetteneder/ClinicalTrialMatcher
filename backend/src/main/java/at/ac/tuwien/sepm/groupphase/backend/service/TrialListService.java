package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialListDto;

import java.util.List;

public interface TrialListService {

    /**
     * Get all trial lists of the current user.
     *
     * @return list of all trial lists of the current user.
     */
    List<TrialListDto> getOwnTrialLists();

    /**
     * Save a new trial list.
     *
     * @param trialListDto the trial list to save.
     * @return the saved trial list dto.
     */
    TrialListDto saveTrialList(TrialListDto trialListDto);

    /**
     * Add a trial to a trial list.
     *
     * @param id the id of the trial list
     * @param trial the trial to add
     * @return the trial list with the added trial
     */
    TrialListDto addTrialToTrialList(Long id, TrialDto trial);

    /**
     * Delete a trial list.
     *
     * @param id the id of the trial list to delete
     */
    void deleteTrialList(Long id);

    /**
     * Get a trial list by id.
     *
     * @param id the id of the trial list
     * @return the trial list dto with the given id
     */
    TrialListDto getTrialListById(Long id);

    /**
     * Delete a trial from a trial list.
     *
     * @param trialId the id of the trial to delete
     * @param listId the id of the trial list where delete should happen
     * @return the trial list dto after deleting the trial
     */
    TrialListDto deleteTrialFromTrialList(Long trialId, Long listId);



}
