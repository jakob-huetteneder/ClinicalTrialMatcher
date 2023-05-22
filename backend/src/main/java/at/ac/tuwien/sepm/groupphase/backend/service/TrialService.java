package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;

import java.util.List;

public interface TrialService {

    public List<Trial> getAllTrials();
    public List<Trial> getOwnTrials();


    public Trial findTrialById(Long id);

    public TrialDto saveTrial(TrialDto trial);

    public TrialDto updateTrial(TrialDto trial);

    public void deleteTrialById(Long id);

}
