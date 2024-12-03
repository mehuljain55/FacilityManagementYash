package com.FacilitiesManager.Controller;

import com.FacilitiesManager.Entity.Enums.AccessRole;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Model.ApiRequestModelBooking;
import com.FacilitiesManager.Entity.Model.ApiRequestViewModel;
import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Entity.User;
import com.FacilitiesManager.Service.CabinRequestService;
import com.FacilitiesManager.Service.UserAuthorizationService;
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


}
