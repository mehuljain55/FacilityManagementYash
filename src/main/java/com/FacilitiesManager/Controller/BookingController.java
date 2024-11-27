package com.FacilitiesManager.Controller;

import com.FacilitiesManager.Entity.Bookings;
import com.FacilitiesManager.Entity.Enums.AccessRole;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Model.ApiRequestModelBooking;
import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Service.BookingService;
import com.FacilitiesManager.Service.CabinRequestService;
import com.FacilitiesManager.Service.UserAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserAuthorizationService userAuthorizationService;

    @Autowired
    private CabinRequestService cabinRequestService;

    private final AccessRole accessRole=AccessRole.manager;

    @PostMapping("/approveBooking")
    public ApiResponseModel createBooking(@RequestBody ApiRequestModelBooking booking)
    {
        boolean validateAccess=userAuthorizationService.validateUserAccess(booking.getUser(),booking.getToken(),accessRole);
        if(validateAccess)
        {
            ApiResponseModel apiResponseModel=bookingService.createBooking(booking.getCabinRequestModel());
            return apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }

    @GetMapping("/viewRequest")
    public  ApiResponseModel viewBookingRequest(@RequestBody ApiRequestModelBooking bookingRequest)
    {
        boolean validateAccess=userAuthorizationService.validateUserAccess(bookingRequest.getUser(),bookingRequest.getToken(),accessRole);
        if(validateAccess)
        {
            ApiResponseModel apiResponseModel=cabinRequestService.getAllCabinRequest(bookingRequest.getOfficeId());
            return apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }

}
