package com.FacilitiesManager.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailingService {

    @Autowired
    private final JavaMailSender mailSender;

    public MailingService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public  void sendMail (String to, String subject, String body, String cc, String bcc) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(createCustomTemplate(body), true); // Use custom template
        helper.setFrom("Yash Technologies <noreply.yashtechnologies@gmail.com>");


        if (cc != null && !cc.isEmpty()) {
            helper.setCc(cc.split(","));
        }
        if (bcc != null && !bcc.isEmpty()) {
            helper.setBcc(bcc.split(","));
        }

        mailSender.send(message);

    }

    private String createCustomTemplate(String body) {
        return """
                <html>
                    <body>
                        <h2 style="color: #007bff;">Yash Technologies</h2>
                        <p style="font-size: 14px;">%s</p>
                        <footer style="margin-top: 20px; font-size: 12px; color: #555;">
                            <p>Best Regards,<br>Yash Technologies</p>
                        </footer>
                    </body>
                </html>
               """.formatted(body);
    }
}

