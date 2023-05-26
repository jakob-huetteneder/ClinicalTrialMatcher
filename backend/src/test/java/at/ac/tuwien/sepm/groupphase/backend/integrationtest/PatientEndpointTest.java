package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.datagenerator.PatientDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "generateData"})
@AutoConfigureMockMvc
public class PatientEndpointTest {

    private static final String USER_BASE_URI = "/api/v1/patients";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PatientDataGenerator patientDataGenerator;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() {
        patientRepository.deleteAll();
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

        //should no longer be accessible

        assertTrue(patientRepository.findById(patient.getId()).isEmpty());
    }

    @Test
    public void testDeleteSpecificPatientError() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(USER_BASE_URI + "/" + -1))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void testCreatePatient() throws Exception {
        Patient patient = patientDataGenerator.generatePatient();
        MvcResult mvcResult = this.mockMvc.perform(post(USER_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patient)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        PatientDto patientDto = objectMapper.readValue(response.getContentAsString(), PatientDto.class);

        assertEquals(patientDto.firstName(), patient.getFirstName());
        assertEquals(patientDto.lastName(), patient.getLastName());
        assertEquals(patientDto.birthdate(), patient.getBirthdate());
    }
}
