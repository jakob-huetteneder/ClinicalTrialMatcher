package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.MedicalImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicalImageRepository extends JpaRepository<MedicalImage, Long> {

    Optional<MedicalImage> findMedicalImageByExamination_Id(Long id);

}
