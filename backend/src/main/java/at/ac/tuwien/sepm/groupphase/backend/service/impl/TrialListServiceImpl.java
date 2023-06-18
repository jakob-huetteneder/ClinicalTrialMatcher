package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialListDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TrialListMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TrialMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import at.ac.tuwien.sepm.groupphase.backend.entity.TrialList;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.TrialListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TrialRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.TrialListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class TrialListServiceImpl implements TrialListService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final TrialListRepository trialListRepository;
    private final TrialRepository trialRepository;
    private final TrialMapper trialMapper;
    private final TrialListMapper trialListMapper;
    private final AuthorizationService authorizationService;
    private final UserRepository userRepository;



    public TrialListServiceImpl(AuthorizationService authorizationService, TrialListRepository trialListRepository,
                                TrialListMapper trialListMapper, UserRepository userRepository, TrialMapper trialMapper,
                                TrialRepository trialRepository) {
        this.authorizationService = authorizationService;
        this.trialListRepository = trialListRepository;
        this.trialListMapper = trialListMapper;
        this.userRepository = userRepository;
        this.trialMapper = trialMapper;
        this.trialRepository = trialRepository;
    }


    @Override
    public List<TrialListDto> getOwnTrialLists() {
        LOG.trace("getAllTrialLists()");
        Long userId = authorizationService.getSessionUserId();
        var trialLists = trialListRepository.getTrialListsByUser_Id(userId);
        LOG.debug("Retrieved own trial lists ({})", trialLists.size());
        return trialListMapper.trialListsToTrialListDtos(trialLists);
    }

    @Override
    public TrialListDto saveTrialList(TrialListDto trialList) {
        LOG.trace("saveTrialList()");
        TrialList convertedTrials = trialListMapper.trialListDtoToTrialList(trialList);
        ApplicationUser loggedInUser = userRepository.findById(authorizationService.getSessionUserId())
            .orElseThrow(() -> new NotFoundException("Could not find a user for the logged in user."));
        convertedTrials.setUser(loggedInUser);
        TrialList savedTrialList = trialListRepository.save(convertedTrials);
        LOG.debug("Saved trials in your list");
        return trialListMapper.trialListToTrialListDto(savedTrialList);
    }

    @Override
    public TrialListDto addTrialToTrialList(Long id, TrialDto trial) {
        LOG.trace("addTrialToTrialList()");
        TrialList trialList = trialListRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Could not find a trial list with the id " + id + "."));
        Trial convertedTrial = trialMapper.trialDtoToTrial(trial);
        trialList.addTrial(convertedTrial);
        TrialList savedTrialList = trialListRepository.save(trialList);
        LOG.debug("Added trial to trial list");
        return trialListMapper.trialListToTrialListDto(savedTrialList);
    }

    @Override
    public void deleteTrialList(Long id) {
        LOG.trace("deleteTrialList()");
        TrialList trialList = trialListRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Could not find a trial list with the id " + id + "."));
        trialListRepository.delete(trialList);
        LOG.debug("Deleted trial list " +  trialList.getName());
    }

    @Override
    public TrialListDto getTrialListById(Long id) {
        LOG.trace("getTrialListById()");
        TrialList trialList = trialListRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Could not find a trial list with the id " + id + "."));
        LOG.debug("Found trial list " +  trialList.getName());
        return trialListMapper.trialListToTrialListDto(trialList);
    }

    @Override
    public TrialListDto deleteTrialFromTrialList(Long trialId, Long listId) {
        LOG.trace("deleteTrialFromTrialList()");
        TrialList deleteTrial = trialListRepository.findById(listId)
            .orElseThrow(() -> new NotFoundException("Could not find a trial with the id " + trialId + "."));
        deleteTrial.getTrial().removeIf(trial -> trial.getId().equals(trialId));
        TrialList savedTrialList = trialListRepository.save(deleteTrial);
        LOG.debug("Deleted trial from trial list");
        return trialListMapper.trialListToTrialListDto(savedTrialList);
    }
}
