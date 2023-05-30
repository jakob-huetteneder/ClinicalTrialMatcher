package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiseaseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.DiseaseMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
public class DiseaseMapperTest {

    @Autowired
    private DiseaseMapper diseaseMapper;

    @Test
    public void testDiseaseToDiseaseDto() {
        Disease disease = new Disease().setName("Diabetes mellitus").setSynonyms("Zuckerkrankheit").setId(1L);

        DiseaseDto diseaseDto = diseaseMapper.diseaseToDiseaseDto(disease);

        assertAll(
            () -> assertEquals(diseaseDto.id(), disease.getId()),
            () -> assertEquals(diseaseDto.name(), disease.getName()),
            () -> assertEquals(diseaseDto.synonyms(), disease.getSynonyms())
        );
    }

}

