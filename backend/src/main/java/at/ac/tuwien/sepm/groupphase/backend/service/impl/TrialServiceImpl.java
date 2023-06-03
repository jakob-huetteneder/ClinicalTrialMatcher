package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TrialMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Researcher;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.TrialRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.TrialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;


@Service
public class TrialServiceImpl implements TrialService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final TrialRepository trialRepository;
    private final TrialMapper trialMapper;
    private final AuthorizationService authorizationService;
    private final UserRepository userRepository;


    public TrialServiceImpl(TrialRepository trialRepository, TrialMapper trialMapper, AuthorizationService authorizationService, UserRepository userRepository) {
        this.trialRepository = trialRepository;
        this.trialMapper = trialMapper;
        this.authorizationService = authorizationService;
        this.userRepository = userRepository;
    }

    @Override
    public List<TrialDto> getAllTrials() {
        LOG.trace("getAllTrials()");
        List<Trial> trials = trialRepository.findAll();
        LOG.info("Retrieved all trials ({})", trials.size());
        return trialMapper.trialToTrialDto(trials);
    }

    @Override
    public List<TrialDto> getOwnTrials() {
        LOG.trace("getAllTrials()");
        Long researcherId = authorizationService.getSessionUserId();
        var trials = trialRepository.getTrialByResearcher_Id(researcherId);
        LOG.info("Retrieved own trials ({})", trials.size());
        return trialMapper.trialToTrialDto(trials);
    }

    @Override
    public TrialDto findTrialById(Long id) {
        LOG.trace("findTrialById()");
        Optional<Trial> trial = trialRepository.findById(id);
        return trialMapper.trialToTrialDto(trial.orElseThrow(() -> new NotFoundException("Trial does not exist.")));
    }

    @Override
    public TrialDto saveTrial(TrialDto trial) {
        LOG.trace("saveTrial()");
        Trial convertedTrial = trialMapper.trialDtoToTrial(trial);
        ApplicationUser loggedInUser = userRepository.findById(authorizationService.getSessionUserId())
            .orElseThrow(() -> new NotFoundException("Could not find a user for the logged in user."));
        if (!(loggedInUser instanceof Researcher)) {
            throw new NotFoundException("Could not find a researcher for the logged in user.");
        }
        convertedTrial.setResearcher(
            (Researcher) loggedInUser);
        Trial savedTrial = trialRepository.save(convertedTrial);
        LOG.info("Saved trial with id='{}'", convertedTrial.getId());
        return trialMapper.trialToTrialDto(savedTrial);
    }

    @Override
    public TrialDto updateTrial(TrialDto trial) {
        LOG.trace("updateTrial()");
        Trial convertedTrial = trialMapper.trialDtoToTrial(trial);
        Trial updatedTrial = trialRepository.save(convertedTrial);
        LOG.info("Updated trial with id='{}'", convertedTrial.getId());
        return trialMapper.trialToTrialDto(updatedTrial);
    }

    @Override
    public void deleteTrialById(Long id) {
        LOG.trace("deleteTrial()");
        // check if researcher responsible for the trial is the same as the logged in user
        Long researcherId = authorizationService.getSessionUserId();
        var trials = trialRepository.getTrialByResearcher_Id(researcherId);
        if (trials.stream().noneMatch(trial -> trial.getId().equals(id))) {
            throw new IllegalArgumentException("The user deleting the trial has to be the person in charge.");
        }

        trialRepository.deleteById(id);
        LOG.info("Deleted trial with id='{}'", id);
    }


}
