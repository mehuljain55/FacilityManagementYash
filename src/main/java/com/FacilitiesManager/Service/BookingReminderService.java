package com.FacilitiesManager.Service;

import com.FacilitiesManager.Entity.Bookings;
import com.FacilitiesManager.Entity.Enums.BookingStatus;
import com.FacilitiesManager.Repository.BookingRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;

public class BookingReminderService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private  MailingService mailingService;

    @Scheduled(cron = "0 0/15 9-23 * * *")
    public void bookingRemainderMail() throws MessagingException {
        List<Bookings> bookings=bookingRepository.findBookingsExpiringSoon(new Date(), BookingStatus.approved);

        for(Bookings booking:bookings)
        {
            System.out.println("Bookings: "+booking);
            String content="This is a reminder that your cabin booking, identified by Booking ID:" +booking.getBookingId()+" Cabin ID:"+booking.getCabinId()+" and Cabin Name "+booking.getCabinName()+" will conclude in exactly 15 minutes. " +
                    "We kindly request you to finalize any activities before the booking period ends. " +
                    "Should you require an extension or have any questions, please feel free to contact the administration team.";
          String body=mailingService.createCustomTemplate(content);
          mailingService.sendMailIndividual(booking.getUserId(),"Booking remainder",body);
        }

    }

}
