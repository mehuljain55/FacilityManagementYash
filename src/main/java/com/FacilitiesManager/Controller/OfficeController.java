package com.FacilitiesManager.Controller;


import com.FacilitiesManager.Entity.Enums.AccessRole;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Model.ApiRequestOfficeAddModel;
import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Service.BookingReminderService;
import com.FacilitiesManager.Service.UserAuthorizationService;
import com.FacilitiesManager.Service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/office")
public class OfficeController {

    @Autowired
    private UserAuthorizationService userAuthorizationService;

    private AccessRole accessRole=AccessRole.super_admin;

    @Autowired
    private UserService userService;


    @GetMapping("/officeList")
    public ApiResponseModel getAllOfficeList()
    {
        return userService.findAllOffice();
    }

    @GetMapping("/findAllOffice")
    public ApiResponseModel getAllOffice()
    {
        return userService.getAllOffice();
    }



    @PostMapping("/add")
    public ApiResponseModel addOfficeLocation(@RequestBody ApiRequestOfficeAddModel officeModel)
    {
        boolean validateAccess=userAuthorizationService.validateUserToken(officeModel.getUser().getEmailId(),officeModel.getToken());
        if(validateAccess)
        {
            ApiResponseModel apiResponseModel=userService.addOffice(officeModel.getOffices());
            return apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }

    @PostMapping("/update")
    public ApiResponseModel updateOffice(@RequestBody ApiRequestOfficeAddModel officeModel)
    {
        boolean validateAccess=userAuthorizationService.validateUserToken(officeModel.getUser().getEmailId(),officeModel.getToken());
        if(validateAccess)
        {
            ApiResponseModel apiResponseModel=userService.updateOffice(officeModel.getOffices());
            return apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }
}
