package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.MedicalImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for medical images
 */
public interface MedicalImageRepository extends JpaRepository<MedicalImage, Long> {

    /**
     * Find medical image by examination id
     *
     * @param id of the examination
     * @return medical image
     */
    Optional<MedicalImage> findMedicalImageByExamination_Id(Long id);

}
