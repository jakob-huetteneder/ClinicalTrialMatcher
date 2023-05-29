package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.annotation.Resource;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import org.awaitility.Awaitility;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles("test")
class EmailServiceTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
        .withConfiguration(GreenMailConfiguration.aConfig().withUser("user", "admin"))
        .withPerMethodLifecycle(false);

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Resource
    private JavaMailSenderImpl emailSender;
    private GreenMail testSmtp;

    @Test
    void sendVerificationEmail() throws JSONException, MessagingException, InterruptedException, IOException {
        JSONObject emailJsonObject = new JSONObject();
        emailJsonObject.put("email", "tester@spring.com");
        emailJsonObject.put("password", "test");
        emailJsonObject.put("firstName", "Angela");
        emailJsonObject.put("lastName", "Pesendorfer");
        emailJsonObject.put("role", Role.DOCTOR);
        emailJsonObject.put("admin", false);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> emailRequest = new HttpEntity<>(emailJsonObject.toString(), headers);

        // Act
        ResponseEntity<UserDetailDto> response = testRestTemplate.postForEntity("/api/v1/users?url="+ URLEncoder.encode("test/#/", StandardCharsets.UTF_8), emailRequest, UserDetailDto.class);

        // Assert
        Assertions.assertEquals(200, response.getStatusCode().value());

        Awaitility.await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
            greenMail.purgeEmailFromAllMailboxes();

            Assertions.assertEquals(1, receivedMessage.getAllRecipients().length);
            Assertions.assertEquals("tester@spring.com", receivedMessage.getAllRecipients()[0].toString());
            Assertions.assertEquals("Clinical Matcher <clinical.matcher@gmail.com>", receivedMessage.getFrom()[0].toString());
            Assertions.assertEquals("Please verify your registration", receivedMessage.getSubject());
        });
    }

    @Test
    void setPasswordEmail() throws JSONException, MessagingException, InterruptedException, IOException {
        JSONObject emailJsonObject = new JSONObject();
        emailJsonObject.put("email", "tester@spring.com");
        emailJsonObject.put("password", "test");
        emailJsonObject.put("firstName", "Angela");
        emailJsonObject.put("lastName", "Pesendorfer");
        emailJsonObject.put("role", Role.DOCTOR);
        emailJsonObject.put("admin", true);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> emailRequest = new HttpEntity<>(emailJsonObject.toString(), headers);

        // Act
        ResponseEntity<UserDetailDto> response = testRestTemplate.postForEntity("/api/v1/users?url="+ URLEncoder.encode("test/#/", StandardCharsets.UTF_8), emailRequest, UserDetailDto.class);

        // Assert
        Assertions.assertEquals(200, response.getStatusCode().value());

        Awaitility.await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
            greenMail.purgeEmailFromAllMailboxes();

            Assertions.assertEquals(1, receivedMessage.getAllRecipients().length);
            Assertions.assertEquals("tester@spring.com", receivedMessage.getAllRecipients()[0].toString());
            Assertions.assertEquals("Clinical Matcher <clinical.matcher@gmail.com>", receivedMessage.getFrom()[0].toString());
            Assertions.assertEquals("Please set your password", receivedMessage.getSubject());
        });
    }
}