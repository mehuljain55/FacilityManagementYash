package com.FacilitiesManager.Service;

import com.FacilitiesManager.Entity.BookingModel;
import com.FacilitiesManager.Entity.Bookings;
import com.FacilitiesManager.Entity.Cabin;
import com.FacilitiesManager.Entity.CabinRequest;
import com.FacilitiesManager.Entity.Enums.BookingStatus;
import com.FacilitiesManager.Entity.Enums.BookingValadity;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Repository.BookingModelRepository;
import com.FacilitiesManager.Repository.BookingRepository;
import com.FacilitiesManager.Repository.CabinRepository;
import com.FacilitiesManager.Repository.CabinRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CabinRepository cabinRepository;

    @Autowired
    private CabinRequestRepository cabinRequestRepository;

    @Autowired
    private BookingModelRepository bookingModelRepository;


     public ApiResponseModel createBooking(CabinRequest cabinRequest)
     {
         try{
             boolean avabality_status;
             if(cabinRequest.getBookingValadity().equals(BookingValadity.single_day))
             {
                 avabality_status=checkCabinAvabalitySingleDay(cabinRequest);
                 cabinRequest.setEndDate(cabinRequest.getStartDate());
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
                     Bookings bookingRequest= bookingRepository.save(bookings);
                     List<Date> dates=getDatesBetween(cabinRequest.getStartDate(),cabinRequest.getEndDate());

                     for(Date date:dates)
                     {
                         BookingModel bookingModel=new BookingModel();
                         bookingModel.setBookingId(bookingRequest.getBookingId());
                         bookingModel.setDate(date);
                         bookingModel.setCabinId(bookingModel.getCabinId());
                         bookingModel.setPurpose(cabinRequest.getPurpose());
                         bookingModel.setUserId(cabinRequest.getUserId());
                         bookingModel.setOfficeId(cabin.getOfficeId());
                         bookingModel.setValidFrom(cabinRequest.getValidFrom());
                         bookingModel.setValidTill(cabinRequest.getValidTill());
                         bookingModelRepository.save(bookingModel);
                     }

                     cabinRequestRepository.save(cabinRequest);
                     return  new ApiResponseModel<>(StatusResponse.success,null,"Booking successfully created");
                 }else {
                     return  new ApiResponseModel<>(StatusResponse.not_found,null,"Cabin not found");
                 }

             }else {
                 return  new ApiResponseModel<>(StatusResponse.not_available,null,"Cabin already booked");
             }

         }catch (Exception e)
         {
             e.printStackTrace();
             return  new ApiResponseModel<>(StatusResponse.failed,null,"Please contact administrator");
         }
     }



    public boolean checkCabinAvabalitySingleDay(CabinRequest cabinRequest) {
        List<BookingModel> bookings = bookingModelRepository.findBookingsByCabinIdSingleDayBetweenTimes(
                cabinRequest.getCabinId(),
                cabinRequest.getValidFrom(),
                cabinRequest.getValidTill(),
                cabinRequest.getStartDate()
        );
        return bookings == null || bookings.isEmpty();
    }

    public boolean checkCabinAvailabilityMultipleDay(CabinRequest cabinRequest) {
        List<BookingModel> bookings = bookingModelRepository.findBookingByCabinIdDate(cabinRequest.getStartDate(),
                                                                             cabinRequest.getEndDate(),
                                                                             cabinRequest.getCabinId());

        return bookings == null || bookings.isEmpty();
    }



    public static List<Date> getDatesBetween(Date startDate, Date endDate)  {
        List<Date> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (!calendar.getTime().after(endDate)) {
            dates.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }


}
