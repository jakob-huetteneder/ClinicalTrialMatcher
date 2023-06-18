package at.ac.tuwien.sepm.groupphase.backend;

import at.ac.tuwien.sepm.groupphase.backend.util.DatabaseUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class TestUtil {

    @Autowired
    private DatabaseUtil databaseUtil;

    /**
     * Clean all tables in the database if the test profile is activated.
     */
    @PostConstruct
    public void cleanAll() {
        databaseUtil.cleanAll();
    }

}
