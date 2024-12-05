package com.FacilitiesManager.Controller;

import com.FacilitiesManager.Entity.Enums.AccessRole;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Model.*;
import com.FacilitiesManager.Entity.User;
import com.FacilitiesManager.Service.CabinRequestService;
import com.FacilitiesManager.Service.UserAuthorizationService;
import com.FacilitiesManager.Service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserAuthorizationService userAuthorizationService;

    @Autowired
    private CabinRequestService cabinRequestService;

    @Autowired
    private UserService userService;


    private final AccessRole accessRole=AccessRole.user;

    @PostMapping("/login")
    public ApiResponseModel validateUser(@RequestParam("userId") String userId,@RequestParam("password") String password)
    {
        ApiResponseModel apiResponseModel=userAuthorizationService.validateUserLogin(userId,password);
        return apiResponseModel;
    }

    @PostMapping("/register")
    public ApiResponseModel validateUser(@RequestBody User user) throws MessagingException {
        System.out.println(user);
        ApiResponseModel apiResponseModel=userAuthorizationService.registerUser(user);
        return apiResponseModel;
    }


    @GetMapping ("/validate_token")
    public ApiResponseModel validateUserToken(@RequestParam("userId") String userId,@RequestParam("token") String token)
    {
        System.out.println("Create Booking");
        boolean status=userAuthorizationService.validateUserToken(userId,token);
        ApiResponseModel apiResponseModel;
        if(status)
        {
             apiResponseModel=new ApiResponseModel<>(StatusResponse.authorized,null,"Valid token");
        }
        else {
            apiResponseModel=new ApiResponseModel<>(StatusResponse.unauthorized,null,"Invalid token");
        }

        return apiResponseModel;
    }



    @PostMapping("/createBooking")
    public ApiResponseModel createCabinBookingRequest(@RequestBody ApiRequestModelBooking bookingModel) throws MessagingException {
        System.out.println("Create Booking");
        boolean validateAccess=userAuthorizationService.validateUserAccess(bookingModel.getUser(),bookingModel.getToken(),accessRole);
        ApiResponseModel apiResponseModel;
        if(validateAccess)
        {
            apiResponseModel=cabinRequestService.createCabinBookingRequest(bookingModel.getCabinRequestModel(),bookingModel.getUser());
            return apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }

    @PostMapping("/viewRequest")
  public  ApiResponseModel viewUserBookingRequest(@RequestBody ApiRequestViewModel requestViewModel)
  {
      System.out.println("View Request");
      boolean validateAccess=userAuthorizationService.validateUserAccess(requestViewModel.getUser(),requestViewModel.getToken(),accessRole);
      ApiResponseModel apiResponseModel;
      if(validateAccess)
      {
          apiResponseModel=cabinRequestService.getCabinRequestByUser(requestViewModel.getUser());
          return apiResponseModel;
      }else {
          return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
      }
  }


  @PostMapping("/allBookingRequestByOfficeId")
  public  ApiResponseModel getBookingList(@RequestBody ApiRequestBookingViewModel apiRequestBookingViewModel)
  {
      System.out.println("Booking List:"+apiRequestBookingViewModel.getUser());
      boolean validateAccess=userAuthorizationService.validateUserAccess(apiRequestBookingViewModel.getUser(),apiRequestBookingViewModel.getToken(),accessRole);
      ApiResponseModel apiResponseModel;
      if(validateAccess)
      {
          apiResponseModel=userService.getAllCabinView(apiRequestBookingViewModel);
          return apiResponseModel;
      }else {
          return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
      }
  }

  @PostMapping ("/officeList")
  public  ApiResponseModel getOfficeList(@RequestBody ApiRequestModel apiRequestModel)
  {
      System.out.println("Office List:"+apiRequestModel.getUser());
      boolean validateAccess=userAuthorizationService.validateUserAccess(apiRequestModel.getUser(),apiRequestModel.getToken(),accessRole);
      ApiResponseModel apiResponseModel;
      if(validateAccess)
      {
          apiResponseModel=userService.findAllOffice();
          return apiResponseModel;
      }else {
          return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
      }
  }


}
