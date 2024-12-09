package com.FacilitiesManager.Controller;

import com.FacilitiesManager.Entity.Bookings;
import com.FacilitiesManager.Entity.Enums.AccessRole;
import com.FacilitiesManager.Entity.Enums.BookingStatus;
import com.FacilitiesManager.Entity.Enums.BookingValadity;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Model.*;
import com.FacilitiesManager.Service.BookingService;
import com.FacilitiesManager.Service.CabinRequestService;
import com.FacilitiesManager.Service.UserAuthorizationService;
import jakarta.mail.MessagingException;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ApiResponseModel createBooking(@RequestBody ApiRequestBooking booking)
    {
        System.out.println(booking);
        boolean validateAccess=userAuthorizationService.validateUserAccess(booking.getUser(),booking.getToken(),accessRole);
        if(validateAccess)
        {
            ApiResponseModel apiResponseModel=bookingService.createBooking(booking.getCabinRequestModel());
            return apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }


    @PostMapping("/viewRequest")
    public  ApiResponseModel viewBookingRequest(@RequestBody ApiRequestViewModel bookingRequest)
    {
        boolean validateAccess=userAuthorizationService.validateUserAccess(bookingRequest.getUser(),bookingRequest.getToken(),accessRole);
        if(validateAccess)
        {
            ApiResponseModel apiResponseModel=cabinRequestService.getAllCabinHoldRequest(bookingRequest.getUser());
            return apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }

    @PostMapping("/viewRequestByDate")
    public  ApiResponseModel viewBookingRequestByDate(@RequestBody ApiRequestViewModel bookingRequest)
    {
        boolean validateAccess=userAuthorizationService.validateUserAccess(bookingRequest.getUser(),bookingRequest.getToken(),accessRole);
        if(validateAccess)
        {
            ApiResponseModel apiResponseModel=cabinRequestService.getAllCabinHoldRequestDate(bookingRequest.getUser(),bookingRequest.getStartDate(),bookingRequest.getEndDate());
            return apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }

    @PostMapping("/cancelRequest")
    public  ApiResponseModel cancelBookingRequest(@RequestBody ApiRequestBooking bookingRequest) throws MessagingException {
        boolean validateAccess=userAuthorizationService.validateUserAccess(bookingRequest.getUser(),bookingRequest.getToken(),accessRole);
        if(validateAccess)
        {
            ApiResponseModel apiResponseModel=bookingService.cancelBookingRequest(bookingRequest.getCabinRequestModel());
            return apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }

    @PostMapping("/viewBooking")
    public  ApiResponseModel viewBooking(@RequestBody ApiRequestModel bookingRequest)
    {
        boolean validateAccess=userAuthorizationService.validateUserAccess(bookingRequest.getUser(),bookingRequest.getToken(),accessRole);
        if(validateAccess)
        {
            ApiResponseModel apiResponseModel=bookingService.viewBooking(bookingRequest.getUser());
            return  apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }

    @PostMapping("/viewUserCabinBooking")
    public ApiResponseModel viewCabinBookingRequest(@RequestBody ApiRequestModelCabinRequest apiRequestModelCabinRequest)
    {
        boolean validateAccess=userAuthorizationService.validateUserAccess(apiRequestModelCabinRequest.getUser(),apiRequestModelCabinRequest.getToken(),accessRole);
        if(validateAccess)
        {
            ApiResponseModel apiResponseModel=bookingService.viewBookingByCabinIdDate(apiRequestModelCabinRequest.getCabinAvaliableModel());
            return  apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }

    @PostMapping("/createVipBooking")
    public  ApiResponseModel createAdminBooking(@RequestBody ApiRequestCabinModifyModel apiRequestCabinModifyModel) throws MessagingException {
        System.out.println("User Details:"+apiRequestCabinModifyModel.getUser().getEmailId());
        boolean validateAccess=userAuthorizationService.validateUserAccess(apiRequestCabinModifyModel.getUser(),apiRequestCabinModifyModel.getToken(),accessRole);
        if(validateAccess)
        {
            ApiResponseModel apiResponseModel=bookingService.createVipBooking(apiRequestCabinModifyModel);
            return  apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }

    @PostMapping("/createReservation")
    public  ApiResponseModel createReservationVipCabin(@RequestBody ApiRequestCabinModifyModel apiRequestCabinModifyModel) throws MessagingException {
        System.out.println(apiRequestCabinModifyModel);
        boolean validateAccess=userAuthorizationService.validateUserAccess(apiRequestCabinModifyModel.getUser(),apiRequestCabinModifyModel.getToken(),accessRole);
        if(validateAccess)
        {
            ApiResponseModel apiResponseModel=bookingService.createVipBooking(apiRequestCabinModifyModel);
            return  apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }

    @PostMapping("/createReservationCustomDate")
    public ApiResponseModel createCabinReservationCustomDate(@RequestBody ApiRequestModelBooking cabinReserveModel) throws MessagingException {
        boolean validateAccess=userAuthorizationService.validateUserAccess(cabinReserveModel.getUser(),cabinReserveModel.getToken(),accessRole);
        ApiResponseModel apiResponseModel;
        if(validateAccess)
        {
            apiResponseModel=cabinRequestService.createCabinReservation(cabinReserveModel.getCabinRequestModel(),cabinReserveModel.getUser());
            return apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }


}