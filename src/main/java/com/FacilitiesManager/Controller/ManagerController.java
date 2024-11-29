package com.FacilitiesManager.Controller;

import com.FacilitiesManager.Entity.Enums.AccessRole;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Model.*;
import com.FacilitiesManager.Entity.User;
import com.FacilitiesManager.Service.CabinRequestService;
import com.FacilitiesManager.Service.UserAuthorizationService;
import com.FacilitiesManager.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthorizationService userAuthorizationService;

    @Autowired
    private CabinRequestService cabinRequestService;


    private final AccessRole accessRole=AccessRole.manager;



    @PostMapping("/userApprovalList")
    public ApiResponseModel<List<User>> getUserApprovalList(@RequestBody ApiRequestModel userRequestModel)
    {
        boolean validateAccess=userAuthorizationService.validateUserAccess(userRequestModel.getUser(),userRequestModel.getToken(),accessRole);
        if(validateAccess)
        {
            ApiResponseModel apiResponseModel=userService.getUserApprovalList(userRequestModel.getUser().getEmailId());
            return apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }

    @PostMapping("/approve")
    public ApiResponseModel<List<User>> approveUser(@RequestBody ApiRequestUserModel userRequestModel)
    {
        boolean validateAccess=userAuthorizationService.validateUserAccess(userRequestModel.getUser(),userRequestModel.getToken(),accessRole);
        if(validateAccess)
        {
            ApiResponseModel apiResponseModel=userService.approveUser(userRequestModel.getEmployeeId());
            return apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }

    @PostMapping("/block")
    public ApiResponseModel<List<User>> blockUser(@RequestBody ApiRequestUserModel userRequestModel)
    {
        boolean validateAccess=userAuthorizationService.validateUserAccess(userRequestModel.getUser(),userRequestModel.getToken(),accessRole);
        if(validateAccess)
        {
            ApiResponseModel apiResponseModel=userService.userApprovalCancel(userRequestModel.getEmployeeId());
            return apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }



    @PostMapping("/addCabin")
    public ApiResponseModel addCabinRequest(@RequestBody ApiReaponseModelCabin cabinRequest)
    {
        boolean validateAccess=userAuthorizationService.validateUserAccess(cabinRequest.getUser(),cabinRequest.getToken(),accessRole);
        if(validateAccess)
        {
            ApiResponseModel apiResponseModel=cabinRequestService.addCabin(cabinRequest.getCabin(),cabinRequest.getUser().getEmailId());
            return apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }

    @PostMapping("/officeRequestSummary")
    public ApiResponseModel getResquestStatusSummary(@RequestBody ApiRequestModel userRequestModel)
    {
        boolean validateAccess=userAuthorizationService.validateUserAccess(userRequestModel.getUser(),userRequestModel.getToken(),accessRole);
        if(validateAccess)
        {
            ApiResponseModel apiResponseModel=userService.getRequestCountModel(userRequestModel.getUser().getOfficeId());
            return apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }
}
