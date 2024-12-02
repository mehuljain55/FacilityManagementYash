//package com.FacilitiesManager.Controller;
//
//import com.FacilitiesManager.Service.MailingService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class MailController {
//
//    @Autowired
//    private MailingService mailingService;
//
//    @GetMapping("/send-email")
//    public String sendEmail(
//            @RequestParam String to,
//            @RequestParam String subject,
//            @RequestParam String body,
//            @RequestParam(required = false) String cc,
//            @RequestParam(required = false) String bcc
//    ) {
//        try {
//            mailingService.sendMail(to, subject, body, cc, bcc);
//            System.out.println("Mail sending initiated");
//            return "Email sending initiated successfully!";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error while initiating email sending: " + e.getMessage();
//        }
//    }
//}
//
