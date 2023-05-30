package at.ac.tuwien.sepm.groupphase.backend.integrationtest;


import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.DiagnosisDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.PatientDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiagnoseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.DiseaseMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiagnosesRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "generateData"})
@AutoConfigureMockMvc
public class DiagnoseEndpointTest {
    private static final String DIAGNOSE_BASE_URI = "/api/v1/patients";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserDataGenerator userDataGenerator;
    @Autowired
    private PatientDataGenerator patientDataGenerator;
    @Autowired
    private DiagnosisDataGenerator diagnosisDataGenerator;

    @Autowired
    private DiagnosesRepository diagnosesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DiseaseMapper diseaseMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private JwtTokenizer jwtTokenizer;

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
        diagnosesRepository.deleteAll();

        userDataGenerator.generateUsers();
        diagnosisDataGenerator.generateDiagnoses();
    }

    @Test
    public void testGetAllDiagnoses() throws Exception {
        Patient patient = patientDataGenerator.generatePatient();
        List<String> patientRoles = new ArrayList<>() {
            {
                add("ROLE_USER");
                add("ROLE_DOCTOR");
            }
        };
        MvcResult mvcResult = this.mockMvc.perform(get(DIAGNOSE_BASE_URI + '/' + patient.getId() + "/diagnose")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(patient.getId().toString(), patientRoles)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<DiagnoseDto> diagnoseDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            DiagnoseDto[].class));

        assertEquals(5, diagnoseDtos.size());
    }

    @Test
    public void testGetAllDiagnosesWithInvalidRole() throws Exception {
        ApplicationUser user = userDataGenerator.generateUser(Role.RESEARCHER);
        user = userRepository.save(user);
        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_RESEARCHER");
            }
        };
        MvcResult mvcResult = this.mockMvc.perform(get(DIAGNOSE_BASE_URI  + '/' + user.getId() + "/diagnose")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getId().toString(), userRoles)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    public void testUpdateDiagnose() throws Exception {
        Patient patient = patientDataGenerator.generatePatient();
        Diagnose diagnose = diagnosisDataGenerator.generateDiagnose();
        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_USER");
                add("ROLE_DOCTOR");
            }
        };
        DiagnoseDto diagnoseDto = new DiagnoseDto (
            diagnose.getId(),
            patient.getId(),
            diseaseMapper.diseaseToDiseaseDto(diagnose.getDisease()),
            diagnose.getDate(),
            "testName"
        );
        MvcResult mvcResult = this.mockMvc.perform(put(DIAGNOSE_BASE_URI + '/' + patient.getId() + "/diagnose" + '/' + diagnose.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(patient.getId().toString(), userRoles))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(diagnoseDto)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        Diagnose updatedDiagnose = diagnosesRepository.findById(diagnose.getId()).orElse(null);
        assertEquals(diagnoseDto.note(), (updatedDiagnose != null ? updatedDiagnose.getNote() : ""));
    }

    @Test
    public void updateSpecificDiagnoseAsDoctor() throws Exception {
        Patient patient = patientDataGenerator.generatePatient();
        ApplicationUser doctor = userDataGenerator.generateUser(Role.DOCTOR);
        doctor = userRepository.save(doctor);
        Diagnose diagnose = diagnosisDataGenerator.generateDiagnose();
        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_USER");
                add("ROLE_DOCTOR");
            }
        };
        DiagnoseDto diagnoseDto = new DiagnoseDto (
            diagnose.getId(),
            patient.getId(),
            diseaseMapper.diseaseToDiseaseDto(diagnose.getDisease()),
            diagnose.getDate(),
            "testName"
        );
        MvcResult mvcResult = this.mockMvc.perform(put(DIAGNOSE_BASE_URI + '/' + patient.getId() + "/diagnose" + '/' + diagnose.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(doctor.getId().toString(), userRoles))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(diagnoseDto)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        Diagnose updatedDiagnose = diagnosesRepository.findById(diagnose.getId()).orElse(null);
        assertEquals(diagnoseDto.note(), (updatedDiagnose != null ? updatedDiagnose.getNote() : ""));
    }

    @Test
    public void updateSpecificDiagnoseAsUser() throws Exception {
        Patient patient = patientDataGenerator.generatePatient();
        ApplicationUser user = userDataGenerator.generateUser(Role.PATIENT);
        user = userRepository.save(user);
        Diagnose diagnose = diagnosisDataGenerator.generateDiagnose();
        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_USER");
                add("ROLE_DOCTOR");
            }
        };
        DiagnoseDto diagnoseDto = new DiagnoseDto (
            diagnose.getId(),
            patient.getId(),
            diseaseMapper.diseaseToDiseaseDto(diagnose.getDisease()),
            diagnose.getDate(),
            "testName"
        );
        MvcResult mvcResult = this.mockMvc.perform(put(DIAGNOSE_BASE_URI + '/' + patient.getId() + "/diagnose" + '/' + diagnose.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getId().toString(), userRoles))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(diagnoseDto)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        Diagnose updatedDiagnose = diagnosesRepository.findById(diagnose.getId()).orElse(null);
        assertEquals(diagnoseDto.note(), (updatedDiagnose != null ? updatedDiagnose.getNote() : ""));
    }
}
