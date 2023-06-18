package at.ac.tuwien.sepm.groupphase.backend.integrationtest;


import at.ac.tuwien.sepm.groupphase.backend.TestUtil;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "generateUsers"})
@AutoConfigureMockMvc
public class UserEndpointTest {

    private static final String USER_BASE_URI = "/api/v1/users";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtil testUtil;
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
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void beforeEach() {
        testUtil.cleanAll();
        userDataGenerator.generateUsers();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        ApplicationUser admin = userDataGenerator.generateUser(Role.ADMIN);

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

        assertAll(
            () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
            () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType())
        );

        List<UserDetailDto> userDetailDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            UserDetailDto[].class));

        assertEquals(5, userDetailDtos.size());
    }

    @Test
    public void testGetAllUsersWithInvalidRole() throws Exception {
        ApplicationUser user = userDataGenerator.generateUser(Role.PATIENT);
        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_USER"); // not admin
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
    public void testValidUpdateUser() throws Exception {
        String password = "password";
        ApplicationUser user = userDataGenerator.generateUser(
            "FirstName",
            "LastName",
            "email@email.com",
            passwordEncoder.encode(password),
            Role.PATIENT,
            ApplicationUser.Status.ACTIVE);

        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_USER");
            }
        };
        UserUpdateDto userUpdateDto = new UserUpdateDto(
            user.getId(),
            "UpdatedFirstName",
            "UpdatedLastName",
            "updated@email.com",
            "updatedPassword",
            Role.PATIENT,
            ApplicationUser.Status.SUSPENDED,
            password
        );
        MvcResult mvcResult = this.mockMvc.perform(put(USER_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getId().toString(), userRoles))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateDto)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertAll(
            () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
            () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType())
        );

        ApplicationUser updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertAll(
            () -> assertEquals(userUpdateDto.firstName(), updatedUser.getFirstName()),
            () -> assertEquals(userUpdateDto.lastName(), updatedUser.getLastName()),
            () -> assertEquals(userUpdateDto.email(), updatedUser.getEmail()),
            () -> assertTrue(passwordEncoder.matches(userUpdateDto.password(), updatedUser.getPassword())),
            () -> assertEquals(userUpdateDto.status(), updatedUser.getStatus())
        );
    }

    @Test
    public void testWrongOldPasswordUpdateUser() throws Exception {
        String password = "password";
        ApplicationUser user = userDataGenerator.generateUser(
            "FirstName",
            "LastName",
            "email@email.com",
            passwordEncoder.encode(password),
            Role.PATIENT,
            ApplicationUser.Status.ACTIVE);
        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_USER");
            }
        };
        UserUpdateDto userUpdateDto = new UserUpdateDto(
            user.getId(),
            "UpdatedFirstName",
            "UpdatedLastName",
            "updated@email.com",
            "updatedPassword",
            Role.PATIENT,
            ApplicationUser.Status.SUSPENDED,
            "wrongPassword"
        );
        MvcResult mvcResult = this.mockMvc.perform(put(USER_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getId().toString(), userRoles))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateDto)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertAll(
            () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
            () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType())
        );

        // check if response error message is correct
        String errorMessage = response.getContentAsString();
        assertTrue(errorMessage.contains("Old password is incorrect"));
    }

    @Test
    public void testUpdateSpecificUserAsAdmin() throws Exception {
        String password = "password";
        ApplicationUser user = userDataGenerator.generateUser(
            "FirstName",
            "LastName",
            "email@email.com",
            passwordEncoder.encode(password),
            Role.PATIENT,
            ApplicationUser.Status.ACTIVE);

        ApplicationUser admin = userDataGenerator.generateUser(Role.ADMIN);

        List<String> adminRoles = new ArrayList<>() {
            {
                add("ROLE_ADMIN");
                add("ROLE_USER");
            }
        };
        UserUpdateDto userUpdateDto = new UserUpdateDto(
            user.getId(),
            "UpdatedFirstName",
            "UpdatedLastName",
            "updated@email.com",
            null,
            Role.PATIENT,
            ApplicationUser.Status.SUSPENDED,
            null
        );
        MvcResult mvcResult = this.mockMvc.perform(put(USER_BASE_URI + "/" + user.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(admin.getId().toString(), adminRoles))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateDto)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertAll(
            () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
            () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType())
        );

        ApplicationUser updatedUser = userRepository.findById(user.getId()).orElseThrow();

        assertAll(
            () -> assertEquals(userUpdateDto.firstName(), updatedUser.getFirstName()),
            () -> assertEquals(userUpdateDto.lastName(), updatedUser.getLastName()),
            () -> assertEquals(userUpdateDto.email(), updatedUser.getEmail()),
            () -> assertEquals(userUpdateDto.status(), updatedUser.getStatus())
        );
    }

    @Test
    public void testUpdateSpecificUserWithInvalidRole() throws Exception {
        String password = "password";
        ApplicationUser user = userDataGenerator.generateUser(
            "FirstName",
            "LastName",
            "email@email.com",
            passwordEncoder.encode(password),
            Role.PATIENT,
            ApplicationUser.Status.ACTIVE);

        ApplicationUser invalidRoleUser = userDataGenerator.generateUser(Role.ADMIN);
        List<String> adminRoles = new ArrayList<>() {
            {
                add("ROLE_USER"); // not admin
            }
        };
        UserUpdateDto userUpdateDto = new UserUpdateDto(
            user.getId(),
            "UpdatedFirstName",
            "UpdatedLastName",
            "updated@email.com",
            null,
            Role.PATIENT,
            ApplicationUser.Status.SUSPENDED,
            null
        );
        MvcResult mvcResult = this.mockMvc.perform(put(USER_BASE_URI + "/" + user.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(invalidRoleUser.getId().toString(), adminRoles))
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
        String password = "password";
        ApplicationUser user = userDataGenerator.generateUser(
            "FirstName",
            "LastName",
            "email@email.com",
            passwordEncoder.encode(password),
            Role.PATIENT,
            ApplicationUser.Status.ACTIVE);

        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_USER");
            }
        };

        MvcResult mvcResult = this.mockMvc.perform(delete(USER_BASE_URI + '/' + user.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getId().toString(), userRoles)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertAll(
            () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
            () -> assertTrue(userRepository.findById(user.getId()).isEmpty())
        );
    }

    @Test
    public void verifyUser() throws Exception {
        ApplicationUser user = userDataGenerator.generateUser(Role.PATIENT);
        user.setStatus(ApplicationUser.Status.ACTION_REQUIRED);
        user = userRepository.save(user);

        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_USER");
            }
        };

        ApplicationUser applicationUser = userRepository.findById(user.getId()).orElseThrow();
        assertEquals(applicationUser.getStatus(), ApplicationUser.Status.ACTION_REQUIRED);
        assertEquals(applicationUser.getVerification(), user.getVerification());

        MvcResult mvcResult = this.mockMvc.perform(get(USER_BASE_URI + "/verify?code=" + user.getVerification()+ "&role="+ Role.PATIENT + "&url="+ URLEncoder.encode("test/#/", StandardCharsets.UTF_8))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getId().toString(), userRoles)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.MOVED_TEMPORARILY.value(), response.getStatus());
        ApplicationUser updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertEquals(updatedUser.getStatus(), ApplicationUser.Status.ACTIVE);
        assertNull(updatedUser.getVerification());
    }

    @Test
    public void failVerifyUser() throws Exception {
        ApplicationUser user = userDataGenerator.generateUser(Role.PATIENT);
        user.setStatus(ApplicationUser.Status.ACTION_REQUIRED);
        user = userRepository.save(user);

        List<String> userRoles = new ArrayList<>() {
            {
                add("ROLE_USER");
            }
        };

        ApplicationUser applicationUser = userRepository.findById(user.getId()).orElseThrow();
        assertEquals(applicationUser.getStatus(), ApplicationUser.Status.ACTION_REQUIRED);
        assertEquals(applicationUser.getVerification(), user.getVerification());

        MvcResult mvcResult = this.mockMvc.perform(get(USER_BASE_URI + "/verify?code=test" + "&role="+ Role.PATIENT + "&url="+ URLEncoder.encode("test/#/", StandardCharsets.UTF_8))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getId().toString(), userRoles)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        ApplicationUser updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertEquals(updatedUser.getStatus(), ApplicationUser.Status.ACTION_REQUIRED);
        assertEquals(applicationUser.getVerification(), user.getVerification());
    }
}
