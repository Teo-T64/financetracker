package in.teotunjic.financetracker.service;


import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;
    public void sendEmail(String to, String subject, String body){
        try{
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromEmail);
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);
            javaMailSender.send(msg);

        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }
    public void sendEmailWithAttachment(String to, String subject, String body, byte[] attachment, String fileName) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = multipart

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);

            helper.addAttachment(fileName, new ByteArrayResource(attachment));

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error sending email with attachment: " + e.getMessage());
        }
    }
}
