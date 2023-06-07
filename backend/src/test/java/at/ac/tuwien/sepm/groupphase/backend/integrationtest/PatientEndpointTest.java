package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.TestUtil;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.DiagnosisDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.PatientDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PatientMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiagnosesRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
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

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "generatePatients"})
@AutoConfigureMockMvc
public class PatientEndpointTest {

    private static final String USER_BASE_URI = "/api/v1/patients";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private PatientDataGenerator patientDataGenerator;
    @Autowired
    private DiagnosisDataGenerator diagnosisDataGenerator;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private PatientMapper patientMapper;
    @Autowired
    private DiagnosesRepository diagnosesRepository;
    @Autowired
    private UserDataGenerator userDataGenerator;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private JwtTokenizer jwtTokenizer;

    @BeforeEach
    public void beforeEach() {
        testUtil.cleanAll();
        patientDataGenerator.generatePatients();
    }

    @Test
    public void testGetSpecificPatient() throws Exception {
        Patient patient = patientDataGenerator.generatePatient();
        patient = patientRepository.save(patient);
        MvcResult mvcResult = this.mockMvc.perform(get(USER_BASE_URI + "/" + patient.getId()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        PatientDto patientDto = objectMapper.readValue(response.getContentAsString(), PatientDto.class);

        assertEquals(patientDto.id(), patient.getId());
        assertEquals(patientDto.firstName(), patient.getFirstName());
        assertEquals(patientDto.lastName(), patient.getLastName());
        assertEquals(patientDto.birthdate(), patient.getBirthdate());

    }

    @Test
    public void testGetSpecificPatientError() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(USER_BASE_URI + "/-1"))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void testDeleteSpecificPatient() throws Exception {
        Patient patient = patientDataGenerator.generatePatient();
        patient = patientRepository.save(patient);
        diagnosisDataGenerator.generateDiagnose(patient);
        assertEquals(1, diagnosesRepository.findAll().size());
        MvcResult mvcResult = this.mockMvc.perform(delete(USER_BASE_URI + "/" + patient.getId()))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        PatientDto patientDto = objectMapper.readValue(response.getContentAsString(), PatientDto.class);

        assertEquals(patientDto.id(), patient.getId());
        assertEquals(patientDto.firstName(), patient.getFirstName());
        assertEquals(patientDto.lastName(), patient.getLastName());
        assertEquals(patientDto.birthdate(), patient.getBirthdate());

        assertEquals(0, diagnosesRepository.findAll().size());


        //should no longer be accessible

        assertTrue(patientRepository.findById(patient.getId()).isEmpty());
    }

    @Test
    public void testDeleteSpecificPatientError() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(USER_BASE_URI + "/" + -1))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void testCreatePatient() throws Exception {
        Doctor doctor = (Doctor) userDataGenerator.generateUser(Role.DOCTOR);

        List<String> doctorRoles = new ArrayList<>() {
            {
                add("ROLE_DOCTOR");
                add("ROLE_USER");
            }
        };


        PatientDto patient = patientMapper.patientToPatientDto(patientDataGenerator.generatePatient());
        MvcResult mvcResult = this.mockMvc.perform(post(USER_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patient))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(doctor.getId().toString(), doctorRoles)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        PatientDto patientDto = objectMapper.readValue(response.getContentAsString(), PatientDto.class);

        assertEquals(patientDto.firstName(), patient.firstName());
        assertEquals(patientDto.lastName(), patient.lastName());
        assertEquals(patientDto.birthdate(), patient.birthdate());
    }
}
