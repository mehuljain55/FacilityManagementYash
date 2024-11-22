package com.FacilitiesManager.Service;

import com.FacilitiesManager.Entity.Bookings;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

     public ApiResponseModel createBooking(Bookings bookingRequest)
     {
         try{
           Bookings bookings=  bookingRepository.save(bookingRequest);
             return  new ApiResponseModel<>(StatusResponse.success,bookings,"Cabin Alloted Success");
         }catch (Exception e)
         {
             e.printStackTrace();
             return  new ApiResponseModel<>(StatusResponse.failed,null,"Please contact administrator");
         }
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
