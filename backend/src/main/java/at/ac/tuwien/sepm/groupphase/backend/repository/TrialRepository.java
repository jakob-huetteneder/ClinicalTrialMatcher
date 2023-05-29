package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrialRepository extends JpaRepository<Trial, Long> {

    List<Trial> getTrialByResearcher_Id(Long id);


    Trial findByTitle(String title);

}
