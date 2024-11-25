package com.FacilitiesManager.Service;

import com.FacilitiesManager.Entity.BookingModel;
import com.FacilitiesManager.Entity.Bookings;
import com.FacilitiesManager.Entity.Cabin;
import com.FacilitiesManager.Entity.CabinRequest;
import com.FacilitiesManager.Entity.Enums.BookingStatus;
import com.FacilitiesManager.Entity.Enums.BookingValadity;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Entity.Model.CabinAvaliableModel;
import com.FacilitiesManager.Repository.BookingModelRepository;
import com.FacilitiesManager.Repository.BookingRepository;
import com.FacilitiesManager.Repository.CabinRepository;
import com.FacilitiesManager.Repository.CabinRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CabinRequestService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CabinRepository cabinRepository;

    @Autowired
    private CabinRequestRepository cabinRequestRepository;

    @Autowired
    private  BookingService bookingService;

    @Autowired
    private BookingModelRepository bookingModelRepository;



    public ApiResponseModel checkCabinAvailabilitySingleDay(CabinRequest cabinRequest) {

        try {
            List<BookingModel> bookings = bookingModelRepository.findBookingsByCabinIdSingleDayBetweenTimes(cabinRequest.getCabinId(),cabinRequest.getValidFrom(),cabinRequest.getValidTill(),cabinRequest.getStartDate());
            boolean isAvailable = bookings == null || bookings.isEmpty();
            StatusResponse status = isAvailable ? StatusResponse.available : StatusResponse.not_available;
            String message = isAvailable ? "Cabin available" : "Cabin not available";
            return new ApiResponseModel<>(status, null, message);
        } catch (Exception e)
        {
            e.printStackTrace();
            return new ApiResponseModel<>(StatusResponse.failed,null,"Unable to process request try again later");
        }
    }

    public ApiResponseModel checkCabinAvailabilityMultipleDay(CabinRequest cabinRequest) {

        try {
            List<Bookings> bookings = bookingRepository.findBookingsBetweenDates(cabinRequest.getStartDate(),cabinRequest.getEndDate(),cabinRequest.getCabinId());
            boolean isAvailable = bookings == null || bookings.isEmpty();
            StatusResponse status = isAvailable ? StatusResponse.available : StatusResponse.not_available;
            String message = isAvailable ? "Cabin available" : "Cabin not available";
            return new ApiResponseModel<>(status, null, message);
        } catch (Exception e)
        {
            e.printStackTrace();
            return new ApiResponseModel<>(StatusResponse.failed,null,"Unable to process request try again later");
        }
    }

    public ApiResponseModel createCabinBookingRequest(CabinRequest cabinRequest)
    {
        boolean isCabinAvailable;
        if(cabinRequest.getBookingValadity().equals(BookingValadity.single_day))
        {
            isCabinAvailable=bookingService.checkCabinAvabalitySingleDay(cabinRequest);
            cabinRequest.setEndDate(cabinRequest.getStartDate());

        }else{
            isCabinAvailable=bookingService.checkCabinAvailabilityMultipleDay(cabinRequest);
            cabinRequest.setValidFrom(LocalTime.of(0,0));
            cabinRequest.setValidTill(LocalTime.of(0,0));
        }

        if(isCabinAvailable)
        {
            cabinRequest.setStatus(BookingStatus.hold);
            cabinRequestRepository.save(cabinRequest);
            return new ApiResponseModel<>(StatusResponse.success,null,"Cabin Request");
        }else {
            return new ApiResponseModel<>(StatusResponse.not_available,null,"Cabin not available");
        }
    }

    public ApiResponseModel<List<Cabin>> getAvailableCabin(CabinAvaliableModel cabinAvaliableModel)
    {
        List<Cabin> cabins=cabinRepository.findCabinByOfficeId(cabinAvaliableModel.getOfficeId());
        List<BookingModel> bookings;

        if(cabins==null)
        {
            return new ApiResponseModel<>(StatusResponse.not_found,null,"No Cabin found");
        }

        if(cabinAvaliableModel.getBookingValadity().equals(BookingValadity.single_day))
        {
            bookings=bookingModelRepository.findBookingsByOfficeIdBetweenTimes(cabinAvaliableModel.getOfficeId(),
                    cabinAvaliableModel.getValidFrom(),
                    cabinAvaliableModel.getValidTill(),
                    cabinAvaliableModel.getStartDate());
            System.out.println("Cabin Model:"+cabinAvaliableModel);
            System.out.println("Booking Size:"+ bookings.size());
        }else {
            bookings=bookingModelRepository.findBookingsMultipleDaysBetweenDates(cabinAvaliableModel.getStartDate(),
                    cabinAvaliableModel.getEndDate(),
                    cabinAvaliableModel.getOfficeId());
        }
        if(bookings!=null)
        {
            for(Cabin cabin:cabins)
            {
                for(BookingModel booking:bookings)
                {
                    if(cabin.getCabinId()==booking.getCabinId())
                    {
                        cabins.remove(cabin);
                    }
                }
            }
            return new ApiResponseModel<>(StatusResponse.success,cabins,"Available Cabin");
        }
        return new ApiResponseModel<>(StatusResponse.success,cabins,"Available Cabin");
    }






}
