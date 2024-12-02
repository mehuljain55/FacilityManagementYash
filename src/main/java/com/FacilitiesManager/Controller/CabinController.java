package com.FacilitiesManager.Controller;

import com.FacilitiesManager.Entity.Cabin;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Model.ApiRequestModel;
import com.FacilitiesManager.Entity.Model.ApiRequestModelCabinRequest;
import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Service.CabinRequestService;
import com.FacilitiesManager.Service.UserAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cabin")
public class CabinController {

    @Autowired
    private CabinRequestService cabinRequestService;

    @Autowired
    private UserAuthorizationService userAuthorizationService;


    @PostMapping ("/findAvailableCabins")
    public ApiResponseModel<List<Cabin>> getAvailableCabin(@RequestBody ApiRequestModelCabinRequest cabinModel)
    {
        boolean validateAccess=userAuthorizationService.validateUserToken(cabinModel.getUser().getEmailId(),cabinModel.getToken());
        if(validateAccess)
        {ApiResponseModel apiResponseModel=cabinRequestService.getAvailableCabin(cabinModel.getCabinAvaliableModel());
            return apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }






}
