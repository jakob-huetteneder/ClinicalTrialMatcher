package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.TrialDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import at.ac.tuwien.sepm.groupphase.backend.repository.TrialRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "generateData"})
@AutoConfigureMockMvc
public class TrialEndpointTest {
    private static final String TRIAL_BASE_URI = "/api/v1/trials";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TrialDataGenerator trialDataGenerator;
    @Autowired
    private TrialRepository trialRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private JwtTokenizer jwtTokenizer;

    @BeforeEach
    public void beforeEach() {
        trialRepository.deleteAll();
        trialDataGenerator.generateTrials();
    }

    @Test
    public void testGetAllTrials() throws Exception {
        Trial trial = trialDataGenerator.generateTrial();
        trial = trialRepository.save(trial);
        List<String> roles = new ArrayList<>() {
            {
                add("ROLE_RESEARCHER");
            }
        };
        MvcResult mvcResult = this.mockMvc.perform(get(TRIAL_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(trial.getResearcher().getId().toString(), roles)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        List<TrialDto> trialDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            TrialDto[].class));
        assertEquals(11, trialDtos.size());
    }

    @Test
    public void testGetAllTrialsWithInvalidRole() throws Exception {
        Trial trial = trialDataGenerator.generateTrial();
        trial = trialRepository.save(trial);
        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_DOCTOR");
            }
        };
        MvcResult mvcResult = this.mockMvc.perform(get(TRIAL_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(trial.getResearcher().getId().toString(), userRoles)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }


    @Test
    public void testGetAllTrialsWithInvalidToken() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(TRIAL_BASE_URI)
                .header(securityProperties.getAuthHeader(), "invalidToken"))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    public void testPostValidTrial() throws Exception {
        Trial trial = trialDataGenerator.generateTrial();

        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_RESEARCHER");
            }
        };
        MvcResult mvcResult = this.mockMvc.perform(post(TRIAL_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trial))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(trial.getResearcher().getId().toString(), userRoles)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    public void testPostInvalidTrial() throws Exception {
        Trial trial = trialDataGenerator.generateTrial();
        trial.setTitle("");
        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_RESEARCHER");
            }
        };
        MvcResult mvcResult = this.mockMvc.perform(post(TRIAL_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trial))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(trial.getResearcher().getId().toString(), userRoles)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }




}