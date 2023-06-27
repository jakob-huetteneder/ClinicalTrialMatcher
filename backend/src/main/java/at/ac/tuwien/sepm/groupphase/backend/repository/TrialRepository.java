package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    /*@Query(
        value = "SELECT * FROM"
            + "(SELECT * FROM trial WHERE LOWER(brief_summary) LIKE CONCAT('%', :keyword, '%') OR LOWER(collaborator) LIKE CONCAT('%', :keyword, '%') OR LOWER"
            + "(cr_free_text) LIKE CONCAT('%', :keyword, '%') OR LOWER(cr_gender) LIKE CONCAT('%', :keyword, '%') OR LOWER(cr_max_age) LIKE CONCAT('%', :keyword, '%') OR LOWER"
            + "(cr_min_age) LIKE CONCAT('%', :keyword, '%') OR LOWER(detailed_summary) LIKE CONCAT('%', :keyword, '%') OR LOWER(end_date) LIKE CONCAT('%', :keyword, '%') OR "
            + "LOWER(location) LIKE CONCAT('%', :keyword, '%') OR LOWER(sponsor) LIKE CONCAT('%', :keyword, '%') OR LOWER(start_date) LIKE CONCAT('%', :keyword, '%') OR LOWER"
            + "(status) LIKE CONCAT('%', :keyword, '%') OR LOWER(study_type) LIKE CONCAT('%', :keyword, '%') OR LOWER(title) LIKE CONCAT('%', :keyword, '%')) "
            + "WHERE ((:male is null or cr_gender = :male) OR (:female is null or cr_gender = :female) OR"
            + " (:both is null or cr_gender = :both)) AND ((:recruiting is null or status = :recruiting) OR (:recruiting is null "
            + "or status = :recruiting)) AND (:minAge = 0 or cr_min_age <= :minAge) AND (:maxAge = 0 or cr_min_age >= :maxAge)"
            + "AND (:startDate is null or start_date >= :startDate) AND (:endDate is null or end_date <= :endDate)",
        nativeQuery = true)
    Page<Trial> search(@Param("keyword") String keyword, @Param("male") String male, @Param("female") String female, @Param("both") String both, @Param(
        "recruiting") String recruiting,
                       @Param("minAge") int minAge, @Param("maxAge") int maxAge, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                       Pageable pageable);*/

    @Query(
        value = "SELECT t FROM Trial t "
        + "WHERE (LOWER(t.briefSummary) LIKE CONCAT('%', :keyword, '%') OR LOWER(t.collaborator) LIKE CONCAT('%', :keyword, '%')"
        + " OR LOWER(t.detailedSummary) LIKE CONCAT('%', :keyword, '%') OR "
        + "LOWER(t.location) LIKE CONCAT('%', :keyword, '%') OR LOWER(t.sponsor) LIKE CONCAT('%', :keyword, '%') "
        + "OR LOWER(t.studyType) LIKE CONCAT('%', :keyword, '%') OR LOWER(t.title) LIKE CONCAT('%', :keyword, '%')) AND"
        + " ((:gender is null or t.crGender = :gender) OR (t.crGender = 2)) AND (:recruiting is null or t.status = :recruiting) AND "
        + "(t.status <> 2) AND (:age is null or (:age <= t.crMaxAge AND :age >= t.crMinAge))"
        + "AND (:startDate is null or t.startDate >= :startDate) AND (:endDate is null or t.endDate <= :endDate)")
    Page<Trial> search(@Param("keyword") String keyword, @Param("gender") Gender gender, @Param("recruiting") Trial.Status recruiting,
                       @Param("age") Integer age, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                       Pageable pageable);

}
