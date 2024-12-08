package com.FacilitiesManager.Controller;

import com.FacilitiesManager.Entity.Enums.AccessRole;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Model.ApiRequestModel;
import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Entity.Model.UserRequestModel;
import com.FacilitiesManager.Service.UserAuthorizationService;
import com.FacilitiesManager.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/super_admin")
public class SuperAdminController {

    @Autowired
    private UserAuthorizationService userAuthorizationService;

    @Autowired
    private UserService userService;

    private AccessRole accessRole=AccessRole.super_admin;

    @PostMapping("/userList")
    public ApiResponseModel findUserByOfficeIdRole(@RequestBody ApiRequestModel apiRequestModel)
    {
        boolean validateAccess=userAuthorizationService.validateUserAccess(apiRequestModel.getUser(),apiRequestModel.getToken(),accessRole);
        ApiResponseModel apiResponseModel;
        if(validateAccess)
        {
            apiResponseModel=userService.findAllUserByOffice(apiRequestModel);
            return apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }

    @PostMapping("/updateDetail")
    public ApiResponseModel updateUserDetail(@RequestBody UserRequestModel userRequestModel)
    {
        boolean validateAccess=userAuthorizationService.validateUserAccess(userRequestModel.getUser(),userRequestModel.getToken(),accessRole);
        ApiResponseModel apiResponseModel;
        if(validateAccess)
        {
            System.out.println("User update details");
            apiResponseModel=userService.updateUserDetail(userRequestModel);
            return apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }

}
