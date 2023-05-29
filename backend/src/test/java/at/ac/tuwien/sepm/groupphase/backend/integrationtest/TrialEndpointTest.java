package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.TrialDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Researcher;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "generateTrials", "generateUsers"})
@AutoConfigureMockMvc
public class TrialEndpointTest {
    private static final String TRIAL_BASE_URI = "/api/v1/trials";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TrialDataGenerator trialDataGenerator;
    @Autowired
    private UserDataGenerator userDataGenerator;
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
    public void testGetOwnTrialsForResearcher() throws Exception {
        Researcher researcher = (Researcher) userDataGenerator.generateUser(Role.RESEARCHER);
        Trial trial = trialDataGenerator.generateTrial(researcher);
        trialDataGenerator.generateTrial(researcher); // second trial for same researcher
        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_RESEARCHER");
            }
        };
        MvcResult mvcResult = this.mockMvc.perform(get(TRIAL_BASE_URI + "/researcher")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(trial.getResearcher().getId().toString(), userRoles)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertAll(
            () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
            () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType())
        );

        List<TrialDto> trialDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(), TrialDto[].class));

        assertAll(
            () -> assertEquals(2, trialDtos.size()),
            () -> assertEquals(trial.getId(), trialDtos.get(0).id()),
            () -> assertEquals(trial.getTitle(), trialDtos.get(0).title()),
            () -> assertEquals(trial.getStartDate(), trialDtos.get(0).startDate()),
            () -> assertEquals(trial.getEndDate(), trialDtos.get(0).endDate()),
            () -> assertEquals(trial.getResearcher().getId(), trialDtos.get(0).researcher().getId()),
            () -> assertEquals(trial.getStudyType(), trialDtos.get(0).studyType()),
            () -> assertEquals(trial.getBriefSummary(), trialDtos.get(0).briefSummary()),
            () -> assertEquals(trial.getDetailedSummary(), trialDtos.get(0).detailedSummary()),
            () -> assertEquals(trial.getSponsor(), trialDtos.get(0).sponsor()),
            () -> assertEquals(trial.getCollaborator(), trialDtos.get(0).collaborator()),
            () -> assertEquals(trial.getStatus(), trialDtos.get(0).status()),
            () -> assertEquals(trial.getLocation(), trialDtos.get(0).location()),
            () -> assertEquals(trial.getCrGender(), trialDtos.get(0).crGender()),
            () -> assertEquals(trial.getCrMinAge(), trialDtos.get(0).crMinAge()),
            () -> assertEquals(trial.getCrMaxAge(), trialDtos.get(0).crMaxAge()),
            () -> assertEquals(trial.getCrFreeText(), trialDtos.get(0).crFreeText())
        );
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

    @Test
    public void testDeleteTrialWithId() throws Exception {
        long count = trialRepository.count();
        Trial trial = trialDataGenerator.generateTrial();
        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_RESEARCHER");
            }
        };
        MvcResult mvcResult = this.mockMvc.perform(delete(TRIAL_BASE_URI + "/" + trial.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(trial.getResearcher().getId().toString(), userRoles)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

        assertEquals(count, trialRepository.count());
    }

    @Test
    public void testDeleteTrialWithInvalidUser() throws Exception {
        Trial trial = trialDataGenerator.generateTrial();
        Researcher otherResearcher = (Researcher) userDataGenerator.generateUser(Role.RESEARCHER);
        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_RESEARCHER");
            }
        };
        MvcResult mvcResult = this.mockMvc.perform(delete(TRIAL_BASE_URI + "/" + (trial.getId() + 1))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(otherResearcher.getId().toString(), userRoles)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }

}