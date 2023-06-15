package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.TrialStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrialRepository extends JpaRepository<Trial, Long> {

    List<Trial> getTrialByResearcher_Id(Long id);


    Trial findByTitle(String title);

    /*@Query(
        value = "SELECT * FROM"
            + "(SELECT * FROM trial WHERE LOWER(brief_summary) LIKE %:keyword% OR LOWER(collaborator) LIKE %:keyword% OR LOWER"
            + "(cr_free_text) LIKE %:keyword% OR LOWER(cr_gender) LIKE %:keyword% OR LOWER(cr_max_age) LIKE %:keyword% OR LOWER"
            + "(cr_min_age) LIKE %:keyword% OR LOWER(detailed_summary) LIKE %:keyword% OR LOWER(end_date) LIKE %:keyword% OR "
            + "LOWER(location) LIKE %:keyword% OR LOWER(sponsor) LIKE %:keyword% OR LOWER(start_date) LIKE %:keyword% OR LOWER"
            + "(status) LIKE %:keyword% OR LOWER(study_type) LIKE %:keyword% OR LOWER(title) LIKE %:keyword%) "
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
        value = "SELECT * FROM"
            + "(SELECT * FROM trial WHERE LOWER(brief_summary) LIKE %:keyword% OR LOWER(collaborator) LIKE %:keyword% OR "
            + "LOWER(cr_gender) LIKE %:keyword% OR LOWER(cr_max_age) LIKE %:keyword% OR LOWER"
            + "(cr_min_age) LIKE %:keyword% OR LOWER(detailed_summary) LIKE %:keyword% OR LOWER(end_date) LIKE %:keyword% OR "
            + "LOWER(location) LIKE %:keyword% OR LOWER(sponsor) LIKE %:keyword% OR LOWER(start_date) LIKE %:keyword% OR LOWER"
            + "(status) LIKE %:keyword% OR LOWER(study_type) LIKE %:keyword% OR LOWER(title) LIKE %:keyword%) "
            + "WHERE ((:gender is null or cr_gender = :gender) OR (cr_gender = 2)) AND ((:recruiting is null or status = :recruiting) OR (:recruiting is null "
            + "or status = :recruiting)) AND (:minAge = 0 or cr_min_age <= :minAge) AND (:maxAge = 0 or cr_max_age >= :maxAge)"
            + "AND (:startDate is null or start_date >= :startDate) AND (:endDate is null or end_date <= :endDate)",
        nativeQuery = true)
    Page<Trial> search(@Param("keyword") String keyword, @Param("gender") String gender, @Param("recruiting") String recruiting,
                       @Param("minAge") int minAge, @Param("maxAge") int maxAge, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                       Pageable pageable);

}
