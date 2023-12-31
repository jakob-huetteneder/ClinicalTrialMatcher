package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.FilterDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.DiseaseMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TrialMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import at.ac.tuwien.sepm.groupphase.backend.entity.Researcher;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiseaseRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TrialRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthorizationService;
import at.ac.tuwien.sepm.groupphase.backend.service.AdmissionNoteAnalyzerService;
import at.ac.tuwien.sepm.groupphase.backend.service.DiseasesService;
import at.ac.tuwien.sepm.groupphase.backend.service.TrialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    private final DiseaseRepository diseaseRepository;
    private final DiseaseMapper diseaseMapper;
    private final DiseasesService diseasesService;
    private final AdmissionNoteAnalyzerService admissionNoteAnalyzerService;


    public TrialServiceImpl(TrialRepository trialRepository, TrialMapper trialMapper, AuthorizationService authorizationService,
                            UserRepository userRepository, DiseaseRepository diseaseRepository, DiseaseMapper diseaseMapper, DiseasesService diseasesService,
                            AdmissionNoteAnalyzerService admissionNoteAnalyzerService) {
        this.trialRepository = trialRepository;
        this.trialMapper = trialMapper;
        this.authorizationService = authorizationService;
        this.userRepository = userRepository;
        this.diseaseRepository = diseaseRepository;
        this.diseaseMapper = diseaseMapper;
        this.diseasesService = diseasesService;
        this.admissionNoteAnalyzerService = admissionNoteAnalyzerService;
    }

    @Override
    public List<TrialDto> getAllTrials() {
        LOG.trace("getAllTrials()");
        List<Trial> trials = trialRepository.findAll();
        LOG.debug("Retrieved all trials ({})", trials.size());
        return trialMapper.trialToTrialDto(trials.stream().toList());
    }

    @Override
    public List<TrialDto> getOwnTrials() {
        LOG.trace("getAllTrials()");
        Long researcherId = authorizationService.getSessionUserId();
        var trials = trialRepository.getTrialByResearcher_Id(researcherId);
        LOG.debug("Retrieved own trials ({})", trials.size());
        return trialMapper.trialToTrialDto(trials);
    }

    @Override
    public TrialDto findTrialById(Long id) {
        LOG.trace("findTrialById({})", id);
        Optional<Trial> trial = trialRepository.findById(id);
        return trialMapper.trialToTrialDto(trial.orElseThrow(() -> new NotFoundException("Trial does not exist.")));
    }

    @Override
    public TrialDto saveTrial(TrialDto trial) {
        LOG.trace("saveTrial({})", trial);
        Trial convertedTrial = trialMapper.trialDtoToTrial(trial);
        ApplicationUser loggedInUser = userRepository.findById(authorizationService.getSessionUserId())
            .orElseThrow(() -> new NotFoundException("Could not find a user for the logged in user."));
        if (!(loggedInUser instanceof Researcher)) {
            throw new NotFoundException("Could not find a researcher for the logged in user.");
        }
        convertedTrial.setResearcher(
            (Researcher) loggedInUser);
        convertedTrial.setDiseases(analyzeSummaryForDiseases(trial));
        Trial savedTrial = trialRepository.save(convertedTrial);
        LOG.debug("Saved trial with id='{}'", convertedTrial.getId());
        return trialMapper.trialToTrialDto(savedTrial);
    }

    @Override
    public TrialDto updateTrial(TrialDto trial) {
        LOG.trace("updateTrial({})", trial);
        List<Disease> recognizedDiseases = analyzeSummaryForDiseases(trial);
        Trial convertedTrial = trialMapper.trialDtoToTrial(trial);
        convertedTrial.setDiseases(recognizedDiseases);
        Trial updatedTrial = trialRepository.save(convertedTrial);
        LOG.debug("Updated trial with id='{}'", convertedTrial.getId());
        return trialMapper.trialToTrialDto(updatedTrial);
    }

    private List<Disease> analyzeSummaryForDiseases(TrialDto trial) {

        return admissionNoteAnalyzerService.extractDiseases(
            trial.briefSummary() + " " + trial.detailedSummary()
        ).stream().map(diseaseDto -> {
            Disease disease = diseaseMapper.diseaseDtoToDisease(diseaseDto);
            disease = diseaseMapper.diseaseDtoToDisease(diseasesService.getPersistedDiseaseWithLink(disease));
            return disease;
        }).toList();
    }

    @Override
    public void deleteTrialById(Long id) {
        LOG.trace("deleteTrial({})", id);
        // check if researcher responsible for the trial is the same as the logged in user
        Long researcherId = authorizationService.getSessionUserId();
        var trials = trialRepository.getTrialByResearcher_Id(researcherId);
        if (trials.stream().noneMatch(trial -> trial.getId().equals(id))) {
            throw new IllegalArgumentException("The user deleting the trial has to be the person in charge.");
        }

        trialRepository.deleteById(id);
        LOG.debug("Deleted trial with id='{}'", id);
    }

    @Override
    public Page<TrialDto> searchWithFilter(String keyword, FilterDto filterDto) {
        LOG.trace("searchWithFilter({}, {})", keyword, filterDto);
        PageRequest req = PageRequest.of(filterDto.page() - 1, filterDto.size());
        Gender gender = filterDto.gender();
        Trial.Status status = filterDto.recruiting();

        Page<Trial> trials = trialRepository.search(keyword.toLowerCase(), gender, status,
            filterDto.age(), filterDto.startDate(), filterDto.endDate(), req);
        return new PageImpl<>(trialMapper.trialToTrialDto(trials.stream().toList()), req, trials.getTotalElements());
    }

}
