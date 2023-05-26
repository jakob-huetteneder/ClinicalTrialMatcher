package at.ac.tuwien.sepm.groupphase.backend.integrationtest;


import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.ExaminationDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.PatientDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ExaminationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.repository.ExaminationRepository;
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
public class ExaminationEndpointTest {
    private static final String EXAMINATION_BASE_URI = "/api/v1/patients";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserDataGenerator userDataGenerator;
    @Autowired
    private PatientDataGenerator patientDataGenerator;
    @Autowired
    private ExaminationDataGenerator examinationDataGenerator;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ExaminationRepository examinationRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private JwtTokenizer jwtTokenizer;

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
        examinationRepository.deleteAll();
        userDataGenerator.generateUsers();
        examinationDataGenerator.generateExaminations();
    }

    @Test
    public void testGetAllExaminations() throws Exception {
        Patient patient = patientDataGenerator.generatePatient();
        patient = patientRepository.save(patient);
        List<String> patientRoles = new ArrayList<>() {
            {
                add("ROLE_USER");
                add("ROLE_DOCTOR");
            }
        };
        MvcResult mvcResult = this.mockMvc.perform(get(EXAMINATION_BASE_URI + '/' + patient.getId() + "/examination")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(patient.getId().toString(), patientRoles)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ExaminationDto> examinationDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            ExaminationDto[].class));

        assertEquals(5, examinationDtos.size());
    }

    @Test
    public void testGetAllExaminationsWithInvalidRole() throws Exception {
        ApplicationUser user = userDataGenerator.generateUser(Role.RESEARCHER);
        user = userRepository.save(user);
        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_RESEARCHER");
            }
        };
        MvcResult mvcResult = this.mockMvc.perform(get(EXAMINATION_BASE_URI  + '/' + user.getId() + "/examination")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getId().toString(), userRoles)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    public void testUpdateExamination() throws Exception {
        Patient patient = patientDataGenerator.generatePatient();
        patient = patientRepository.save(patient);
        Examination examination = examinationDataGenerator.generateExamination();
        examination = examinationRepository.save(examination);
        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_USER");
                add("ROLE_DOCTOR");
            }
        };
        ExaminationDto examinationDto = new ExaminationDto (
            examination.getId(),
            patient.getId(),
            "testName",
            examination.getDate(),
            examination.getType(),
            examination.getNote()
        );
        MvcResult mvcResult = this.mockMvc.perform(put(EXAMINATION_BASE_URI + '/' + patient.getId() + "/examination" + '/' + examination.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(patient.getId().toString(), userRoles))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(examinationDto)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        Examination updatedExamination = examinationRepository.findById(examination.getId()).orElse(null);
        assertEquals(examinationDto.name(), (updatedExamination != null ? updatedExamination.getName() : ""));
    }

    @Test
    public void updateSpecificExaminationAsDoctor() throws Exception {
        Patient patient = patientDataGenerator.generatePatient();
        patient = patientRepository.save(patient);
        ApplicationUser doctor = userDataGenerator.generateUser(Role.DOCTOR);
        doctor = userRepository.save(doctor);
        Examination examination = examinationDataGenerator.generateExamination();
        examination = examinationRepository.save(examination);
        List<String> adminRoles = new ArrayList<>() {
            {
                add("ROLE_DOCTOR");
                add("ROLE_USER");
            }
        };
        ExaminationDto examinationDto = new ExaminationDto (
            examination.getId(),
            patient.getId(),
            "testName",
            examination.getDate(),
            examination.getType(),
            examination.getNote()
        );
        MvcResult mvcResult = this.mockMvc.perform(put(EXAMINATION_BASE_URI + '/' + patient.getId() + "/examination" + '/' + examination.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(doctor.getId().toString(), adminRoles))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(examinationDto)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        Examination updatedExamination = examinationRepository.findById(examination.getId()).orElse(null);
        assertEquals(examinationDto.name(),  (updatedExamination != null ? updatedExamination.getName() : ""));
    }

    @Test
    public void updateSpecificUserAsUser() throws Exception {
        Patient patient = patientDataGenerator.generatePatient();
        patient = patientRepository.save(patient);
        ApplicationUser user = userDataGenerator.generateUser(Role.PATIENT);
        user = userRepository.save(user);
        Examination examination = examinationDataGenerator.generateExamination();
        examination = examinationRepository.save(examination);
        List<String> adminRoles = new ArrayList<>() {
            {
                add("ROLE_DOCTOR");
                add("ROLE_USER");
            }
        };
        ExaminationDto examinationDto = new ExaminationDto (
            examination.getId(),
            patient.getId(),
            "testName",
            examination.getDate(),
            examination.getType(),
            examination.getNote()
        );
        MvcResult mvcResult = this.mockMvc.perform(put(EXAMINATION_BASE_URI + '/' + patient.getId() + "/examination" + '/' + examination.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getId().toString(), adminRoles))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(examinationDto)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        Examination updatedExamination = examinationRepository.findById(examination.getId()).orElse(null);
        assertEquals(examinationDto.name(),  (updatedExamination != null ? updatedExamination.getName() : ""));
    }
}
