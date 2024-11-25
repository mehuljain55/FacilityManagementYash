package com.FacilitiesManager.Service;

import com.FacilitiesManager.Entity.Bookings;
import com.FacilitiesManager.Entity.Cabin;
import com.FacilitiesManager.Entity.CabinRequest;
import com.FacilitiesManager.Entity.Enums.BookingStatus;
import com.FacilitiesManager.Entity.Enums.BookingValadity;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Repository.BookingRepository;
import com.FacilitiesManager.Repository.CabinRepository;
import com.FacilitiesManager.Repository.CabinRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CabinRepository cabinRepository;

    @Autowired
    private CabinRequestRepository cabinRequestRepository;


     public ApiResponseModel createBooking(CabinRequest cabinRequest)
     {
         try{
             boolean avabality_status;
             if(cabinRequest.getBookingValadity().equals(BookingValadity.single_day))
             {
                 avabality_status=checkCabinAvabalitySingleDay(cabinRequest);
             }
             else{
                 avabality_status=checkCabinAvailabilityMultipleDay(cabinRequest);
             }

             if(avabality_status)
             {
                 Bookings bookings=new Bookings();
                 Optional<Cabin> opt=cabinRepository.findById(cabinRequest.getCabinId());
                 if(opt.isPresent())
                 {
                     Cabin cabin=opt.get();
                     bookings.setCabinId(cabin.getCabinId());
                     bookings.setPurpose(cabinRequest.getPurpose());
                     bookings.setUserId(cabinRequest.getUserId());
                     bookings.setOfficeId(cabin.getOfficeId());
                     bookings.setStartDate(cabinRequest.getStartDate());
                     bookings.setEndDate(cabinRequest.getEndDate());
                     bookings.setValidFrom(cabinRequest.getValidFrom());
                     bookings.setValidTill(cabinRequest.getValidTill());
                     cabinRequest.setStatus(BookingStatus.approved);
                     bookingRepository.save(bookings);
                     cabinRequestRepository.save(cabinRequest);
                     return  new ApiResponseModel<>(StatusResponse.success,null,"Booking succssfully created");
                 }else {
                     return  new ApiResponseModel<>(StatusResponse.not_found,null,"Cabin not found");
                 }

             }else {
                 return  new ApiResponseModel<>(StatusResponse.not_available,null,"Cablin already booked");
             }

         }catch (Exception e)
         {
             e.printStackTrace();
             return  new ApiResponseModel<>(StatusResponse.failed,null,"Please contact administrator");
         }
     }

    public boolean checkCabinAvabalitySingleDay(CabinRequest cabinRequest) {
        List<Bookings> bookings = bookingRepository.findBookingsByCabinIdSingleDayBetweenTimes(
                cabinRequest.getCabinId(),
                cabinRequest.getValidFrom(),
                cabinRequest.getValidTill(),
                cabinRequest.getStartDate()
        );
        return bookings == null || bookings.isEmpty();
    }

    public boolean checkCabinAvailabilityMultipleDay(CabinRequest cabinRequest) {
        List<Bookings> bookings = bookingRepository.findBookingsBetweenDates(cabinRequest.getStartDate(),
                                                                             cabinRequest.getEndDate(),
                                                                             cabinRequest.getCabinId());

        return bookings == null || bookings.isEmpty();
    }

    public ApiResponseModel viewBookingByDateandOffice(Date date, String officeId)
     {
         List<Bookings> bookings=bookingRepository.getBookingByDateandOffice(officeId,date);
         if(bookings!=null&& bookings.size()>0)
         {
           return new ApiResponseModel<>(StatusResponse.success,bookings,"Booking found");
         }else{
           return new ApiResponseModel<>(StatusResponse.success,null,"No Booking found");
         }
     }
}
