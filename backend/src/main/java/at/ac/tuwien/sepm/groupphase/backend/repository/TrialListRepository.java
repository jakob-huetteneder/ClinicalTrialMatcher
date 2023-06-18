package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.TrialList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrialListRepository extends JpaRepository<TrialList, Long> {
    List<TrialList> getTrialListsByUser_Id(Long id);

    TrialList findByName(String name);

}
