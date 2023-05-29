package at.ac.tuwien.sepm.groupphase.backend.integrationtest;


import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Status;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "generateData"})
@AutoConfigureMockMvc
public class UserEndpointTest {

    private static final String USER_BASE_URI = "/api/v1/users";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserDataGenerator userDataGenerator;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private JwtTokenizer jwtTokenizer;

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
        userDataGenerator.generateUsers();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        ApplicationUser admin = userDataGenerator.generateUser(Role.ADMIN);
        admin = userRepository.save(admin);
        List<String> adminRoles = new ArrayList<>() {
            {
                add("ROLE_ADMIN");
                add("ROLE_USER");
            }
        };
        MvcResult mvcResult = this.mockMvc.perform(get(USER_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(admin.getId().toString(), adminRoles)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<UserDetailDto> userDetailDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            UserDetailDto[].class));

        assertEquals(5, userDetailDtos.size());
    }

    @Test
    public void testGetAllUsersWithInvalidRole() throws Exception {
        ApplicationUser user = userDataGenerator.generateUser(Role.PATIENT);
        user = userRepository.save(user);
        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_USER");
            }
        };
        MvcResult mvcResult = this.mockMvc.perform(get(USER_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getId().toString(), userRoles)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    public void testUpdateUser() throws Exception {
        ApplicationUser user = userDataGenerator.generateUser(Role.PATIENT);
        user = userRepository.save(user);
        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_USER");
            }
        };
        UserUpdateDto userDetailDto = new UserUpdateDto(
            user.getId(),
            "Updated",
            user.getLastName(),
            user.getEmail(),
            user.getPassword(),
            Role.PATIENT,
            Status.ACTIVE,
            null
        );
        MvcResult mvcResult = this.mockMvc.perform(put(USER_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getId().toString(), userRoles))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDetailDto)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        ApplicationUser updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertEquals(userDetailDto.firstName(), updatedUser.getFirstName());
    }

    @Test
    public void updateSpecificUserAsAdmin() throws Exception {
        ApplicationUser user = userDataGenerator.generateUser(Role.PATIENT);
        user = userRepository.save(user);
        ApplicationUser admin = userDataGenerator.generateUser(Role.ADMIN);
        admin = userRepository.save(admin);
        List<String> adminRoles = new ArrayList<>() {
            {
                add("ROLE_ADMIN");
                add("ROLE_USER");
            }
        };
        UserUpdateDto userDetailDto = new UserUpdateDto(
            user.getId(),
            "Updated",
            user.getLastName(),
            user.getEmail(),
            user.getPassword(),
            Role.PATIENT,
            Status.ACTIVE,
            null
        );
        MvcResult mvcResult = this.mockMvc.perform(put(USER_BASE_URI + "/" + user.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(admin.getId().toString(), adminRoles))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDetailDto)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        ApplicationUser updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertEquals(userDetailDto.firstName(), updatedUser.getFirstName());
    }

    @Test
    public void updateSpecificUserAsUser() throws Exception {
        ApplicationUser user = userDataGenerator.generateUser(Role.PATIENT);
        user = userRepository.save(user);
        ApplicationUser admin = userDataGenerator.generateUser(Role.ADMIN);
        admin = userRepository.save(admin);
        List<String> adminRoles = new ArrayList<>() {
            {
                add("ROLE_USER");
            }
        };
        UserUpdateDto userUpdateDto = new UserUpdateDto(
            user.getId(),
            "Updated",
            user.getLastName(),
            user.getEmail(),
            user.getPassword(),
            Role.PATIENT,
            Status.ACTIVE,
            null
        );
        MvcResult mvcResult = this.mockMvc.perform(put(USER_BASE_URI + "/" + user.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(admin.getId().toString(), adminRoles))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateDto)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        System.out.println(response.getContentAsString());
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    public void deleteOwnProfileAsUser() throws Exception {
        ApplicationUser user = userDataGenerator.generateUser(
            Role.PATIENT,
            "Max",
            "Mustermann",
            "max.mustermann@gmail.com",
            "Password");
        user = userRepository.save(user);
        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_USER");
            }
        };

        long id = user.getId();
        assertTrue(userRepository.findById(id).isPresent());
        MvcResult mvcResult = this.mockMvc.perform(delete(USER_BASE_URI + '/' + id)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getId().toString(), userRoles)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertTrue(userRepository.findById(id).isEmpty());
    }

}
