package com.FacilitiesManager.Service;

import com.FacilitiesManager.Entity.Bookings;
import com.FacilitiesManager.Entity.CabinRequest;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Repository.BookingRepository;
import com.FacilitiesManager.Repository.CabinRepository;
import com.FacilitiesManager.Repository.CabinRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class CabinRequestService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CabinRepository cabinRepository;

    @Autowired
    private CabinRequestRepository cabinRequestRepository;


    public ApiResponseModel checkCabinAvailabilitySingleDay(CabinRequest cabinRequest) {

        try {
            List<Bookings> bookings = bookingRepository.findBookingsSingleDayBetweenTimes(cabinRequest.getCabinId(), cabinRequest.getValidFrom(), cabinRequest.getValidTill(), cabinRequest.getStartDate());
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






}
