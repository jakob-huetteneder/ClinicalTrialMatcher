package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for trials.
 */
@Repository
public interface TrialRepository extends JpaRepository<Trial, Long> {

    /**
     * Find all trials of a researcher.
     *
     * @param id of the researcher
     * @return list of trials
     */
    List<Trial> getTrialByResearcher_Id(Long id);

    /**
     * Find trial by title.
     *
     * @param title of the trial
     * @return found trial
     */
    Trial findByTitle(String title);

}
