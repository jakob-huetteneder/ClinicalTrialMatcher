package at.ac.tuwien.sepm.groupphase.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("at.ac.tuwien.sepm.groupphase.backend.repository")
@EnableElasticsearchRepositories("at.ac.tuwien.sepm.groupphase.backend.elasticrepository")
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
