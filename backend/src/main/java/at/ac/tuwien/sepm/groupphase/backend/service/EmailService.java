package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;

@Service
public class EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final JavaMailSender mailSender;
    private final Environment env;

    public EmailService(JavaMailSender mailSender, Environment env) {
        this.mailSender = mailSender;
        this.env = env;
    }

    public void sendVerificationEmail(ApplicationUser user, Role role) {
        LOG.info("Sending verification email to {}", user.getEmail());
        String subject = "Please verify your registration";

        String verifyUrl = env.getProperty("project.backend.url") + "/api/v1/users/verify?code=" + user.getVerification() + "&role=" + role;
        String content = String.format("Dear %s %s,<br>"
            + "Please click the link below to verify your registration:<br>"
            + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
            + "Thank you,<br>"
            + "Clinical Matcher", user.getFirstName(), user.getLastName());

        content = content.replace("[[URL]]", verifyUrl);

        MimeMessage email = createEmail(user.getEmail(), subject, content);
        this.mailSender.send(email);
    }

    public void sendSetPasswordEmail(ApplicationUser user) {
        LOG.info("Sending set password email to {}", user.getEmail());

        String subject = "Please set your password";

        String setPasswordUrl = env.getProperty("project.frontend.url") + "/#/account/set-password/" + user.getVerification();
        String content = String.format("Dear %s %s,<br>"
            + "Please click the link below to set your password:<br>"
            + "<h3><a href=\"%s\" target=\"_self\">SET PASSWORD</a></h3>"
            + "Thank you,<br>"
            + "Clinical Matcher", user.getFirstName(), user.getLastName(), setPasswordUrl);

        MimeMessage email = createEmail(user.getEmail(), subject, content);
        this.mailSender.send(email);
    }

    public MimeMessage createEmail(String toAddress, String subject, String content) {
        MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom("clinical.matcher@gmail.com", "Clinical Matcher");
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(content, true);
        } catch (UnsupportedEncodingException | MessagingException emailException) {
            throw new IllegalArgumentException("Illegal data format.", emailException);
        }
        return message;
    }
}
