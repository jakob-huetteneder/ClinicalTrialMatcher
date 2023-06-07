package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.TestUtil;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.PatientDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.TreatsDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientRequestDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TreatsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Admin;
import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.Treats;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.repository.TreatsRepository;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
public class TreatsEndpointTest {

    private static final String TREATS_BASE_URI = "/api/v1/treats";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private TreatsRepository treatsRepository;
    @Autowired
    private TreatsDataGenerator treatsDataGenerator;
    @Autowired
    private PatientDataGenerator patientDataGenerator;
    @Autowired
    private UserDataGenerator userDataGenerator;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private JwtTokenizer jwtTokenizer;

    private static final List<String> PATIENT_ROLES = new ArrayList<>() {
        {
            add("ROLE_PATIENT");
            add("ROLE_USER");
        }
    };

    private static final List<String> DOCTOR_ROLES = new ArrayList<>() {
        {
            add("ROLE_DOCTOR");
            add("ROLE_USER");
        }
    };

    @BeforeEach
    public void beforeEach() {
        testUtil.cleanAll();
    }

    @Test
    public void testGetAllRequestsForPatient() throws Exception {
        Patient patient = patientDataGenerator.generatePatientWithAccount();

        treatsDataGenerator.generateFor(patient, 10);

        assertEquals(10, treatsRepository.count());

        MvcResult mvcResult = this.mockMvc.perform(get(TREATS_BASE_URI + "/requests")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(patient.getApplicationUser().getId().toString(), PATIENT_ROLES)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertAll(
            () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
            () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType())
        );

        List<TreatsDto> treatsDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(), TreatsDto[].class));

        assertEquals(10, treatsDtos.size());
    }

    @Test
    public void testGetAllRequestsForDoctor() throws Exception {
        Doctor doctor = (Doctor) userDataGenerator.generateUser(Role.DOCTOR);

        treatsDataGenerator.generateFor(doctor, 10);

        assertEquals(10, treatsRepository.count());

        MvcResult mvcResult = this.mockMvc.perform(get(TREATS_BASE_URI + "/requests")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(doctor.getId().toString(), DOCTOR_ROLES)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertAll(
            () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
            () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType())
        );

        List<TreatsDto> treatsDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(), TreatsDto[].class));

        assertEquals(10, treatsDtos.size());
    }

    @Test
    public void testGetAllRequestsForPatientWithInvalidRole() throws Exception {
        Admin admin = (Admin) userDataGenerator.generateUser(Role.ADMIN);

        List<String> adminRoles = new ArrayList<>() {
            {
                add("ROLE_ADMIN");
                add("ROLE_USER");
            }
        };

        MvcResult mvcResult = this.mockMvc.perform(get(TREATS_BASE_URI + "/requests")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(admin.getId().toString(), adminRoles)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    public void testRequestTreatsAsDoctor() throws Exception {
        Doctor doctor = (Doctor) userDataGenerator.generateUser(Role.DOCTOR);
        Patient patient = patientDataGenerator.generatePatientWithAccount();

        MvcResult mvcResult = this.mockMvc.perform(post(TREATS_BASE_URI + "/" + patient.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(doctor.getId().toString(), DOCTOR_ROLES)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertAll(
            () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
            () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType())
        );

        PatientRequestDto patientRequestDto = objectMapper.readValue(response.getContentAsString(), PatientRequestDto.class);

        assertAll(
            () -> assertEquals(doctor.getId(), patientRequestDto.treats().doctor().id()),
            () -> assertEquals(patient.getId(), patientRequestDto.treats().patient().id()),
            () -> assertEquals(Treats.Status.REQUESTED, treatsRepository.findByTreatsId_PatientIdAndTreatsId_DoctorId(patient.getId(), doctor.getId()).get().getStatus())
        );
    }

    @Test
    public void testRequestTreatsWithInvalidRole() throws Exception {
        Admin admin = (Admin) userDataGenerator.generateUser(Role.ADMIN);
        Patient patient = patientDataGenerator.generatePatientWithAccount();

        List<String> adminRoles = new ArrayList<>() {
            {
                add("ROLE_ADMIN");
                add("ROLE_USER");
            }
        };

        MvcResult mvcResult = this.mockMvc.perform(post(TREATS_BASE_URI + "/" + patient.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(admin.getId().toString(), adminRoles)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    public void testRespondToRequestAsPatient() throws Exception {
        Doctor doctor = (Doctor) userDataGenerator.generateUser(Role.DOCTOR);
        Patient patient = patientDataGenerator.generatePatientWithAccount();

        treatsDataGenerator.generateTreatsBetween(patient, doctor, Treats.Status.REQUESTED);

        MvcResult mvcResult = this.mockMvc.perform(put(TREATS_BASE_URI + "/" + doctor.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(patient.getApplicationUser().getId().toString(), PATIENT_ROLES))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(true)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertAll(
            () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
            () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType())
        );

        TreatsDto treatsDto = objectMapper.readValue(response.getContentAsString(), TreatsDto.class);

        assertAll(
            () -> assertEquals(doctor.getId(), treatsDto.doctor().id()),
            () -> assertEquals(patient.getId(), treatsDto.patient().id()),
            () -> assertEquals(Treats.Status.ACCEPTED, treatsRepository.findByTreatsId_PatientIdAndTreatsId_DoctorId(patient.getId(), doctor.getId()).get().getStatus())
        );
    }

    @Test
    public void testRespondToRequestAsPatientWithInvalidRole() throws Exception {
        Admin admin = (Admin) userDataGenerator.generateUser(Role.ADMIN);
        Doctor doctor = (Doctor) userDataGenerator.generateUser(Role.DOCTOR);
        Patient patient = patientDataGenerator.generatePatientWithAccount();

        treatsDataGenerator.generateTreatsBetween(patient, doctor, Treats.Status.REQUESTED);

        List<String> adminRoles = new ArrayList<>() {
            {
                add("ROLE_ADMIN");
                add("ROLE_USER");
            }
        };

        MvcResult mvcResult = this.mockMvc.perform(put(TREATS_BASE_URI + "/" + doctor.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(admin.getId().toString(), adminRoles))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(true)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    public void testDeleteTreatsAsPatientAsPatient() throws Exception {
        Doctor doctor = (Doctor) userDataGenerator.generateUser(Role.DOCTOR);
        Patient patient = patientDataGenerator.generatePatientWithAccount();

        treatsDataGenerator.generateTreatsBetween(patient, doctor, Treats.Status.ACCEPTED);

        MvcResult mvcResult = this.mockMvc.perform(delete(TREATS_BASE_URI + "/" + doctor.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(patient.getApplicationUser().getId().toString(), PATIENT_ROLES)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertFalse(treatsRepository.findByTreatsId_PatientIdAndTreatsId_DoctorId(patient.getId(), doctor.getId()).isPresent());
    }
}
