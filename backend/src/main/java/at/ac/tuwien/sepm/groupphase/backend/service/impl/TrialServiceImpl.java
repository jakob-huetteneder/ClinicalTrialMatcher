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
    public List<Trial> getAllTrials() {
        LOG.trace("getAllTrials()");
        var trials = trialRepository.findAll();
        LOG.info("Retrieved all trials ({})", trials.size());
        return trials;
    }

    @Override
    public Trial findTrialById(Long id) {
        LOG.trace("findTrialById()");
        Optional<Trial> trial = trialRepository.findById(id);
        if (trial.isPresent()) {
            LOG.info("Retrieved trial with id='{}'", trial.get().getId());
            return trial.get();
        }
        throw new NotFoundException(String.format("Could not find trial with id %s", id));
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
        trialRepository.deleteById(id);
        LOG.info("Deleted trial with id='{}'", id);
    }


}
