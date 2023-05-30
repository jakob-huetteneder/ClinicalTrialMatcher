package at.ac.tuwien.sepm.groupphase.backend.integrationtest;


import at.ac.tuwien.sepm.groupphase.backend.TestUtil;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.PatientDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.repository.ExaminationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.MedicalImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "generatePatients"})
@AutoConfigureMockMvc
public class FilesEndpointTest {

    private static final String USER_BASE_URI = "/api/v1/files";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtil testUtil;
    @Autowired
    PatientDataGenerator patientDataGenerator;
    @Autowired
    private MedicalImageRepository imagesRepository;
    @Autowired
    private ExaminationRepository examinationRepository;
    @Autowired
    private PatientRepository patientRepository;

    @BeforeEach
    public void beforeEach() {
        testUtil.cleanAll();
    }

    @Test
    public void testPostSpecificImage() throws Exception {
        Resource fileResource = new ClassPathResource("img.png");

        Patient patient = patientDataGenerator.generatePatient();
        patient = patientRepository.save(patient);
        Examination examination = new Examination().setPatient(patient)
            .setDate(LocalDate.now()).setName("Examination 1").setType("Preliminary");
        examination = examinationRepository.save(examination);

        MockMultipartFile firstFile = new MockMultipartFile(
            "file",fileResource.getFilename(),
            MediaType.MULTIPART_FORM_DATA_VALUE,
            fileResource.getInputStream());

        MvcResult mvcResult = this.mockMvc.perform(multipart(USER_BASE_URI + "/" + examination.getId())
            .file(firstFile)
            )
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        //delete right away

        mvcResult = this.mockMvc.perform(delete(USER_BASE_URI + "/" + examination.getId())
            )
            .andDo(print())
            .andReturn();
        response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        Assertions.assertTrue(imagesRepository.findMedicalImageByExamination_Id(examination.getId()).isEmpty());
    }

    @Test
    public void testPostSpecificImageError() throws Exception {
        Resource fileResource = new ClassPathResource("no_img.txt");

        Patient patient = patientDataGenerator.generatePatient();
        patient = patientRepository.save(patient);
        Examination examination = new Examination().setPatient(patient)
            .setDate(LocalDate.now()).setName("Examination 1").setType("Preliminary");
        examination = examinationRepository.save(examination);

        MockMultipartFile firstFile = new MockMultipartFile(
            "file",fileResource.getFilename(),
            MediaType.MULTIPART_FORM_DATA_VALUE,
            fileResource.getInputStream());

        MvcResult mvcResult = this.mockMvc.perform(multipart(USER_BASE_URI + "/" + examination.getId())
                .file(firstFile)
            )
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), response.getStatus());
    }

    @Test
    public void testGetSpecificImage() throws Exception {
        Resource fileResource = new ClassPathResource("img.png");

        Patient patient = patientDataGenerator.generatePatient();
        patient = patientRepository.save(patient);
        Examination examination = new Examination().setPatient(patient)
            .setDate(LocalDate.now()).setName("Examination 1").setType("Preliminary");
        examination = examinationRepository.save(examination);

        MockMultipartFile firstFile = new MockMultipartFile(
            "file",fileResource.getFilename(),
            MediaType.MULTIPART_FORM_DATA_VALUE,
            fileResource.getInputStream());

        MvcResult mvcResult = this.mockMvc.perform(multipart(USER_BASE_URI + "/" + examination.getId())
                .file(firstFile)
            )
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        //should now be accessible

         mvcResult = this.mockMvc.perform(get(USER_BASE_URI + "/" + examination.getId())
            )
            .andDo(print())
            .andReturn();
        response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        fileResource = new ClassPathResource("img.png");

        assertEquals(fileResource.getContentAsByteArray().length, response.getContentLength());

        //delete right away

        mvcResult = this.mockMvc.perform(delete(USER_BASE_URI + "/" + examination.getId())
            )
            .andDo(print())
            .andReturn();
        response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        Assertions.assertTrue(imagesRepository.findMedicalImageByExamination_Id(examination.getId()).isEmpty());
    }

    @Test
    public void testGetSpecificImageError() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(USER_BASE_URI + "/-1")
            )
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void testDeleteSpecificImage() throws Exception {
        Resource fileResource = new ClassPathResource("img.png");

        Patient patient = patientDataGenerator.generatePatient();
        patient = patientRepository.save(patient);
        Examination examination = new Examination().setPatient(patient)
            .setDate(LocalDate.now()).setName("Examination 1").setType("Preliminary");
        examination = examinationRepository.save(examination);

        MockMultipartFile firstFile = new MockMultipartFile(
            "file",fileResource.getFilename(),
            MediaType.MULTIPART_FORM_DATA_VALUE,
            fileResource.getInputStream());

        MvcResult mvcResult = this.mockMvc.perform(multipart(USER_BASE_URI + "/" + examination.getId())
                .file(firstFile)
            )
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        //should now be accessible

        mvcResult = this.mockMvc.perform(delete(USER_BASE_URI + "/" + examination.getId())
            )
            .andDo(print())
            .andReturn();
        response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        Assertions.assertTrue(imagesRepository.findMedicalImageByExamination_Id(examination.getId()).isEmpty());
    }

    @Test
    public void testDeleteSpecificImageError() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(USER_BASE_URI + "/-1")
            )
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

}
