package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.TestUtil;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.PatientDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.TrialDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
public class MatchingTest {


    private static final String MATCHING_BASE_URI = "/api/v1/trials/match/";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private UserDataGenerator userDataGenerator;
    @Autowired
    private PatientDataGenerator patientDataGenerator;
    @Autowired
    private TrialDataGenerator trialDataGenerator;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private JwtTokenizer jwtTokenizer;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;





    @BeforeEach
    public void beforeEach() {
        testUtil.cleanAll();
    }

    @Test
    public void testMatchingPatientToTrials() throws Exception {
        Doctor doctor = (Doctor) userDataGenerator.generateUser(Role.DOCTOR);

        Patient patient = patientDataGenerator.generatePatient();
        elasticsearchOperations.save(patient);

        Trial trial = trialDataGenerator.generateTrial();

        MvcResult mvcResult = this.mockMvc.perform(get(MATCHING_BASE_URI + trial.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(doctor.getId().toString(), new ArrayList<>() {
                    {
                        add("ROLE_DOCTOR");
                        add("ROLE_USER");
                    }
                })))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        String patients = response.getContentAsString();

        System.out.println(patients);
        assertEquals("[]", patients);
    }
}
