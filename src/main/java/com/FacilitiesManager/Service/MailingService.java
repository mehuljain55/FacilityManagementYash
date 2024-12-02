package com.FacilitiesManager.Service;

import com.FacilitiesManager.Entity.CabinRequest;
import com.FacilitiesManager.Entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailingService {

    @Autowired
    private final JavaMailSender mailSender;

    public MailingService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async("mailTaskExecutor")
    public void sendMail(List<String> to, String subject, String body, String cc) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        if (to != null && !to.isEmpty()) {
            helper.setTo(to.toArray(new String[0]));
        } else {
            throw new IllegalArgumentException("The 'to' field cannot be null or empty.");
        }

        helper.setSubject(subject);
        helper.setText(createCustomTemplate(body), true); // Use custom template
        helper.setFrom("Yash Technologies <noreply.yashtechnologies@gmail.com>");

        if (cc != null && !cc.isEmpty()) {
            helper.setCc(cc.split(","));
        }



        mailSender.send(message);
        System.out.println("Mail sent to: " + String.join(", ", to));
    }

    public String createCustomTemplate(String body) {
        return """
                <html>
                    <body>
                        <h2 style="color: #007bff;">Yash Technologies</h2>
                        <p style="font-size: 14px;">%s</p>
                    
                    </body>
                </html>
               """.formatted(body);
    }

    public String createCabinRequestMail(CabinRequest cabinRequest) {
        return """
        <html>
            <body>
                <h2 style="color: #007bff;">Cabin Request Notification</h2>
                <p style="font-size: 14px;">
                    A new cabin request has been raised with the following details:
                </p>
                <table style="width: 100%%; border-collapse: collapse; font-size: 14px;">
                    <tr>
                        <th style="text-align: left; padding: 8px; border-bottom: 1px solid #ddd;">Field</th>
                        <th style="text-align: left; padding: 8px; border-bottom: 1px solid #ddd;">Value</th>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Request ID</td>
                        <td style="padding: 8px;">%d</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Cabin ID</td>
                        <td style="padding: 8px;">%d</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Cabin Name</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">User ID</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Purpose</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Office ID</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Start Date</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">End Date</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Valid From</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Valid Till</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Booking Validity</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Status</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                </table>
                <footer style="margin-top: 20px; font-size: 12px; color: #555;">
                    <p>Best Regards,<br>Yash Technologies</p>
                </footer>
            </body>
        </html>
        """.formatted(
                cabinRequest.getRequestId(),
                cabinRequest.getCabinId(),
                cabinRequest.getCabinName(),
                cabinRequest.getUserId(),
                cabinRequest.getPurpose(),
                cabinRequest.getOfficeId(),
                cabinRequest.getStartDate(),
                cabinRequest.getEndDate(),
                cabinRequest.getValidFrom(),
                cabinRequest.getValidTill(),
                cabinRequest.getBookingValadity(),
                cabinRequest.getStatus()
        );
    }

    public String userRequest(User user) {
        return """
        <html>
            <body>
                <h2 style="color: #007bff;">User Request Notification</h2>
                <p style="font-size: 14px;">
                    A new user request has been raised with the following details:
                </p>
                <table style="width: 100%%; border-collapse: collapse; font-size: 14px;">
                    <tr>
                        <th style="text-align: left; padding: 8px; border-bottom: 1px solid #ddd;">Field</th>
                        <th style="text-align: left; padding: 8px; border-bottom: 1px solid #ddd;">Value</th>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Email ID</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Name</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                     <tr>
                        <td style="padding: 8px;">Mobile no</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Status</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                        
                </table>
                <footer style="margin-top: 20px; font-size: 12px; color: #555;">
                    <p>Best Regards,<br>Yash Technologies</p>
                </footer>
            </body>
        </html>
        """.formatted(
                user.getEmailId(),
                user.getName(),
                user.getMobileNo(),
                user.getStatus()
        );
    }


    public String createApprovalMail(CabinRequest cabinRequest) {
        return """
        <html>
            <body>
                <h2 style="color: #28a745;">Cabin Request Approved</h2>
                <p style="font-size: 14px;">
                    Dear %s,<br><br>
                    Your cabin request has been <strong style="color: #28a745;">approved</strong>. Below are the details of your request:
                </p>
                <table style="width: 100%%; border-collapse: collapse; font-size: 14px;">
                    <tr>
                        <th style="text-align: left; padding: 8px; border-bottom: 1px solid #ddd;">Field</th>
                        <th style="text-align: left; padding: 8px; border-bottom: 1px solid #ddd;">Value</th>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Request ID</td>
                        <td style="padding: 8px;">%d</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Cabin Name</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Purpose</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Start Date</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">End Date</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Valid From</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Valid Till</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                </table>
                <p style="margin-top: 20px; font-size: 14px;">You can now use the cabin as per the approved timings.</p>
                <footer style="margin-top: 20px; font-size: 12px; color: #555;">
                    <p>Best Regards,<br>Yash Technologies</p>
                </footer>
            </body>
        </html>
        """.formatted(
                cabinRequest.getUserId(), // User's name or ID
                cabinRequest.getRequestId(),
                cabinRequest.getCabinName(),
                cabinRequest.getPurpose(),
                cabinRequest.getStartDate(),
                cabinRequest.getEndDate(),
                cabinRequest.getValidFrom(),
                cabinRequest.getValidTill()
        );
    }


    public String createRejectionMail(CabinRequest cabinRequest) {
        return """
        <html>
            <body>
                <h2 style="color: #dc3545;">Cabin Request Rejected</h2>
                <p style="font-size: 14px;">
                    Dear %s,<br><br>
                    We regret to inform you that your cabin request has been <strong style="color: #dc3545;">rejected</strong>. Below are the details of your request:
                </p>
                <table style="width: 100%%; border-collapse: collapse; font-size: 14px;">
                    <tr>
                        <th style="text-align: left; padding: 8px; border-bottom: 1px solid #ddd;">Field</th>
                        <th style="text-align: left; padding: 8px; border-bottom: 1px solid #ddd;">Value</th>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Request ID</td>
                        <td style="padding: 8px;">%d</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Cabin Name</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Purpose</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">Start Date</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px;">End Date</td>
                        <td style="padding: 8px;">%s</td>
                    </tr>
                </table>
                
                <footer style="margin-top: 20px; font-size: 12px; color: #555;">
                    <p>Best Regards,<br>Yash Technologies</p>
                </footer>
            </body>
        </html>
        """.formatted(
                cabinRequest.getUserId(), // User's name or ID
                cabinRequest.getRequestId(),
                cabinRequest.getCabinName(),
                cabinRequest.getPurpose(),
                cabinRequest.getStartDate(),
                cabinRequest.getEndDate()

        );
    }




}
