package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.TestUtil;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.TrialListDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialListDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Researcher;
import at.ac.tuwien.sepm.groupphase.backend.entity.TrialList;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.repository.TrialRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "generateTrials", "generateUsers"})
@AutoConfigureMockMvc
public class TrialListEndpointTest {
    private static final String TRIAL_LIST_BASE_URI = "/api/v1/trialList";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private TrialListDataGenerator trialListDataGenerator;
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
        testUtil.cleanAll();
        trialListDataGenerator.generateTrialLists();
    }


    @Test
    public void testGetAllTrialsWithInvalidToken() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(TRIAL_LIST_BASE_URI)
                .header(securityProperties.getAuthHeader(), "invalidToken"))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }



    @Test
    public void testGetOwnTrialListsForUser() throws Exception {
        Researcher researcher = (Researcher) userDataGenerator.generateUser(Role.RESEARCHER);
        TrialList trialList = trialListDataGenerator.generateTrialList(researcher);
        trialListDataGenerator.generateTrialList(researcher);
        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_RESEARCHER");
            }
        };
        MvcResult mvcResult = this.mockMvc.perform(get(TRIAL_LIST_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(trialList.getUser().getId().toString(), userRoles)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertAll(
            () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
            () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType())
        );

        List<TrialListDto> trialListDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(), TrialListDto[].class));

        assertAll(
            () -> assertEquals(2, trialListDtos.size()),
            () -> assertEquals(trialList.getId(), trialListDtos.get(0).getId()),
            () -> assertEquals(trialList.getName(), trialListDtos.get(0).getName()),
            () -> assertEquals(trialList.getUser().getId(), trialListDtos.get(0).getUser().getId())
            );
    }

    @Test
    public void testPostValidTrialList() throws Exception {
        TrialList trialList = trialListDataGenerator.generateTrialList();

        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_RESEARCHER");
            }
        };
        MvcResult mvcResult = this.mockMvc.perform(post(TRIAL_LIST_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trialList))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(trialList.getUser().getId().toString(), userRoles)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    public void testPostInValidTrialList() throws Exception {
        TrialList trialList = trialListDataGenerator.generateTrialList();
        trialList.setName("");
        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_RESEARCHER");
            }
        };
        MvcResult mvcResult = this.mockMvc.perform(post(TRIAL_LIST_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trialList))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(trialList.getUser().getId().toString(), userRoles)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }





}