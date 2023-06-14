package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.TestUtil;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.DiseaseDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiseaseRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.DiseasesService;
import io.github.fastily.jwiki.core.Wiki;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "generateDiseases"})
@AutoConfigureMockMvc
public class DiseasesServiceTest {

    @Autowired
    private TestUtil testUtil;
    @Autowired
    private DiseaseRepository diseaseRepository;
    @Autowired
    private DiseasesService diseaseService;
    @Autowired
    private DiseaseDataGenerator diseaseDataGenerator;

    @BeforeEach
    public void beforeEach() {
        testUtil.cleanAll();
    }

    @Test
    public void testSetDiseaseLinkForOneValidDisease() {
        Disease disease = new Disease().setName("Diabetes mellitus");
        String link = "https://en.wikipedia.org/wiki/Diabetes_mellitus";
        diseaseRepository.save(disease);

        diseaseService.setAllDiseaseLinks();
        List<Disease> savedDisease = diseaseRepository.findDiseasesByName("Diabetes mellitus");

        assertEquals(1, savedDisease.size());
        assertEquals("Diabetes mellitus", savedDisease.get(0).getName());
        assertNotNull(savedDisease.get(0).getLink());
        assertEquals(link, savedDisease.get(0).getLink());
    }

    @Test
    public void testSetDiseaseLinkForOneInvalidDisease() {

        Disease disease = new Disease().setName("aosdiansodnaisd");
        diseaseRepository.save(disease);

        diseaseService.setAllDiseaseLinks();
        List<Disease> savedDisease = diseaseRepository.findDiseasesByName("aosdiansodnaisd");

        assertEquals(1, savedDisease.size());
        assertNull(savedDisease.get(0).getLink());
    }

    @Test
    public void testSetDiseaseLinkForMultipleValidDiseases() {
        diseaseDataGenerator.generateDiseases();
        diseaseService.setAllDiseaseLinks();
        Wiki wiki = new Wiki.Builder().build();
        List<Disease> savedDiseases = diseaseRepository.findAll();
        for (Disease disease : savedDiseases) {
            String diseaseName = disease.getName();
            String[] words = diseaseName.split(" ");
            String query = String.join("_", words);
            // query: e.g. "Diabetes mellitus" -> "Diabetes_mellitus"
            String link = "https://en.wikipedia.org/wiki/" + query;
            if (wiki.exists(disease.getName())) assertNotNull(disease.getLink());
            if (wiki.exists(disease.getName())) assertEquals(link, disease.getLink());
            if (!wiki.exists(disease.getName())) assertNull(disease.getLink());
        }
    }
}
