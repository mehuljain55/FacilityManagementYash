package com.FacilitiesManager.Controller;


import com.FacilitiesManager.Entity.Enums.AccessRole;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Model.ApiRequestOfficeAddModel;
import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Service.UserAuthorizationService;
import com.FacilitiesManager.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/office")
public class OfficeController {

    @Autowired
    private UserAuthorizationService userAuthorizationService;

    @Autowired
    private UserService userService;

    private AccessRole accessRole=AccessRole.super_admin;



    @GetMapping("/activeOfficeList")
    public ApiResponseModel getAllActiveOfficeList()
    {
        return userService.findAllActiveOffice();
    }


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
        boolean validateAccess=userAuthorizationService.validateUserAccess(officeModel.getUser(),officeModel.getToken(),accessRole);
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
        boolean validateAccess=userAuthorizationService.validateUserAccess(officeModel.getUser(),officeModel.getToken(),accessRole);
        if(validateAccess)
        {
            ApiResponseModel apiResponseModel=userService.updateOffice(officeModel.getOffices());
            return apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }
}
