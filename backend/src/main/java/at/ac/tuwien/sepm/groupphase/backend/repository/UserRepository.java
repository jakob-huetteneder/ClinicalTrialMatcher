package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for users.
 */
@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Long> {

    /**
     * Find user by email.
     *
     * @param email of the user
     * @return user
     */
    ApplicationUser findByEmail(String email);

    /**
     * Find user by verification.
     *
     * @param verification of the user
     * @return user
     */
    ApplicationUser findByVerification(String verification);
}
