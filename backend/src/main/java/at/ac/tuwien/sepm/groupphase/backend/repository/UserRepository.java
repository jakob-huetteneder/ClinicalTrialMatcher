package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<ApplicationUser, Long> {

    ApplicationUser findByEmail(String email);
}
