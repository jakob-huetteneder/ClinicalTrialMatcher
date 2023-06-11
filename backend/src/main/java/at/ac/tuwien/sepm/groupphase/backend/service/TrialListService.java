package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialListDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.TrialList;

import java.util.List;

public interface TrialListService {

    List<TrialListDto> getOwnTrialLists();

    TrialListDto saveTrialList(TrialListDto trialListDto);

    TrialListDto addTrialToTrialList(Long id, TrialDto trial);

    void deleteTrialList(Long id);

    TrialListDto getTrialListById(Long id);

    TrialListDto deleteTrialFromTrialList(Long trialId, Long listId);



}
