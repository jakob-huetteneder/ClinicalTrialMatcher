package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.TestUtil;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.PatientDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.TrialDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.TrialRegistrationDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.Registration;
import at.ac.tuwien.sepm.groupphase.backend.entity.Researcher;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.repository.TrialRegistrationRepository;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
public class RegistrationEndpointTest {

    private static final String TRIAL_REGISTRATION_BASE_URL = "/api/v1/trials/registration";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private TrialDataGenerator trialDataGenerator;
    @Autowired
    private PatientDataGenerator patientDataGenerator;
    @Autowired
    private UserDataGenerator userDataGenerator;
    @Autowired
    private TrialRegistrationDataGenerator trialRegistrationDataGenerator;
    @Autowired
    private TrialRegistrationRepository trialRegistrationRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private JwtTokenizer jwtTokenizer;

    @BeforeEach
    public void beforeEach() {
        testUtil.cleanAll();
    }

    @Test
    public void testGetAllRegistrationsForTrialAsResearcher() throws Exception {
        Researcher researcher = (Researcher) userDataGenerator.generateUser(Role.RESEARCHER);

        Patient patient = patientDataGenerator.generatePatientWithAccount();
        Trial trial = trialDataGenerator.generateTrial();
        trialRegistrationDataGenerator.generateFor(patient, 10);
        trialRegistrationDataGenerator.generateFor(trial, 10);

        assertEquals(20, trialRegistrationRepository.count());
        List<String> researcherRoles = new ArrayList<>() {
            {
                add("ROLE_RESEARCHER");
                add("ROLE_USER");
            }
        };

        MvcResult mvcResult = this.mockMvc.perform(get(TRIAL_REGISTRATION_BASE_URL + "/" + trial.getId().toString())
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(researcher.getId().toString(), researcherRoles)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertAll(
            () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
            () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType())
        );

        List<TrialRegistrationDto> trialRegistrationDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(), TrialRegistrationDto[].class));

        assertEquals(10, trialRegistrationDtos.size());
    }

    @Test
    public void testRegisterForTrialAsPatient() throws Exception {

        Patient patient = patientDataGenerator.generatePatientWithAccount();
        Trial trial = trialDataGenerator.generateTrial();

        List<String> patientRoles = new ArrayList<>() {
            {
                add("ROLE_PATIENT");
                add("ROLE_USER");
            }
        };

        MvcResult mvcResult = this.mockMvc.perform(post(TRIAL_REGISTRATION_BASE_URL + "/" + trial.getId().toString())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(patient.getApplicationUser().getId().toString(), patientRoles)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertAll(
            () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
            () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType())
        );

        TrialRegistrationDto trialRegistrationDto = objectMapper.readValue(response.getContentAsString(), TrialRegistrationDto.class);

        assertNotNull(trialRegistrationDto);
        assertEquals(trial.getId(), trialRegistrationDto.trial().id());
        assertEquals(patient.getId(), trialRegistrationDto.patient().id());
        assertEquals(Registration.Status.PATIENT_ACCEPTED, trialRegistrationDto.status());
    }

    @Test
    public void testRegisterPatientWithAccountForTrialAsDoctor() throws Exception {
        Doctor doctor = (Doctor) userDataGenerator.generateUser(Role.DOCTOR);
        Patient patient = patientDataGenerator.generatePatientWithAccount();
        Trial trial = trialDataGenerator.generateTrial();

        List<String> doctorRoles = new ArrayList<>() {
            {
                add("ROLE_DOCTOR");
                add("ROLE_USER");
            }
        };

        MvcResult mvcResult = this.mockMvc.perform(post(TRIAL_REGISTRATION_BASE_URL + "/" + trial.getId().toString() + "/patient/" + patient.getId().toString())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(doctor.getId().toString(), doctorRoles)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertAll(
            () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
            () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType())
        );

        TrialRegistrationDto trialRegistrationDto = objectMapper.readValue(response.getContentAsString(), TrialRegistrationDto.class);

        assertNotNull(trialRegistrationDto);
        assertEquals(trial.getId(), trialRegistrationDto.trial().id());
        assertEquals(patient.getId(), trialRegistrationDto.patient().id());
        assertEquals(Registration.Status.PROPOSED, trialRegistrationDto.status());
    }

    @Test
    public void testRegisterPatientWithoutAccountForTrialAsDoctor() throws Exception {
        Doctor doctor = (Doctor) userDataGenerator.generateUser(Role.DOCTOR);
        Patient patient = patientDataGenerator.generatePatient();
        Trial trial = trialDataGenerator.generateTrial();

        List<String> doctorRoles = new ArrayList<>() {
            {
                add("ROLE_DOCTOR");
                add("ROLE_USER");
            }
        };

        MvcResult mvcResult = this.mockMvc.perform(post(TRIAL_REGISTRATION_BASE_URL + "/" + trial.getId().toString() + "/patient/" + patient.getId().toString())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(doctor.getId().toString(), doctorRoles)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertAll(
            () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
            () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType())
        );

        TrialRegistrationDto trialRegistrationDto = objectMapper.readValue(response.getContentAsString(), TrialRegistrationDto.class);

        assertNotNull(trialRegistrationDto);
        assertEquals(trial.getId(), trialRegistrationDto.trial().id());
        assertEquals(patient.getId(), trialRegistrationDto.patient().id());
        assertEquals(Registration.Status.PATIENT_ACCEPTED, trialRegistrationDto.status());
    }
}
