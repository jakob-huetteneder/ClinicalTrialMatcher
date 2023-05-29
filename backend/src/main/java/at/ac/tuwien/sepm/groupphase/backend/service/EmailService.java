package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(ApplicationUser user, String siteUrl, Role role, String redirectUrl) {
        String toAddress = user.getEmail();
        String fromAddress = "clinical.matcher@gmail.com";
        String senderName = "Clinical Matcher";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
            + "Please click the link below to verify your registration:<br>"
            + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
            + "Thank you,<br>"
            + "Clinical Matcher";
        content = content.replace("[[name]]", user.getFirstName() + " " + user.getLastName());
        String verifyUrl = siteUrl + "/verify?code=" + user.getVerification() + "&role=" + role + "&url=" + redirectUrl.substring(0, redirectUrl.indexOf("#"));

        content = content.replace("[[URL]]", verifyUrl);
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

            helper.setText(content, true);

            this.mailSender.send(message);
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPasswordEmail(ApplicationUser user) {
        String toAddress = user.getEmail();
        String fromAddress = "clinical.matcher@gmail.com";
        String senderName = "Clinical Matcher";
        String subject = "Please set your password";
        String content = "Dear [[name]],<br>"
            + "Please click the link below to set your password:<br>"
            + "<h3><a href=\"[[URL]]\" target=\"_self\">SET PASSWORD</a></h3>"
            + "Thank you,<br>"
            + "Clinical Matcher";
        content = content.replace("[[name]]", user.getFirstName() + " " + user.getLastName());
        String verifyUrl = "http://localhost:4200/#/password/password?code=" + user.getVerification();

        content = content.replace("[[URL]]", verifyUrl);
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

            helper.setText(content, true);

            this.mailSender.send(message);
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
