package com.FacilitiesManager.Controller;

import com.FacilitiesManager.Entity.Bookings;
import com.FacilitiesManager.Entity.Enums.AccessRole;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Model.ApiRequestModelBooking;
import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Service.BookingService;
import com.FacilitiesManager.Service.UserAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserAuthorizationService userAuthorizationService;

    private final AccessRole accessRole=AccessRole.manager;

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
}
