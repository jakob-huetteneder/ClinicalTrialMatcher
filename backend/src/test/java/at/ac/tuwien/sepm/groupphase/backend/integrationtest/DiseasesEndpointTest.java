package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.TestUtil;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiseaseDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiseaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "generateDiseases"})
@AutoConfigureMockMvc
public class DiseasesEndpointTest {

    private static final String USER_BASE_URI = "/api/v1/diseases";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DiseaseRepository diseaseRepository;

    @BeforeEach
    public void beforeEach() {
        testUtil.cleanAll();
    }

    @Test
    public void testGetSpecificDisease() throws Exception {
        Disease disease = new Disease().setName("Diabetes mellitus").setLink("https://en.wikipedia.org/wiki/Diabetes");
        diseaseRepository.save(disease);

        MvcResult mvcResult = this.mockMvc.perform(get(USER_BASE_URI + "?name=Dia&limit=5"))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<DiseaseDto> diseaseDto = Arrays.asList(objectMapper.readValue(response.getContentAsString(), DiseaseDto[].class));

        assertFalse(diseaseDto.isEmpty());
        assertEquals(1, diseaseDto.size());
        DiseaseDto recv = diseaseDto.get(0);

        assertEquals(recv.id(), disease.getId());
        assertEquals(recv.name(), disease.getName());
        assertEquals(recv.link(), disease.getLink());
    }

    @Test
    public void testGetSpecificPatientError() throws Exception {
        Disease disease = new Disease().setName("Diabetes mellitus").setLink("https://en.wikipedia.org/wiki/Diabetes");
        diseaseRepository.save(disease);

        MvcResult mvcResult = this.mockMvc.perform(get(USER_BASE_URI + "?name=PV-Syndrome&limit=5"))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<DiseaseDto> diseaseDto = Arrays.asList(objectMapper.readValue(response.getContentAsString(), DiseaseDto[].class));

        assertTrue(diseaseDto.isEmpty());
    }

}
