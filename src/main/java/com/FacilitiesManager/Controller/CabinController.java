package com.FacilitiesManager.Controller;

import com.FacilitiesManager.Entity.Cabin;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Model.ApiRequestModelCabin;
import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Service.CabinRequestService;
import com.FacilitiesManager.Service.UserAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cabin")
public class CabinController {

    @Autowired
    private CabinRequestService cabinRequestService;

    @Autowired
    private UserAuthorizationService userAuthorizationService;


    @GetMapping("/findAvailableCabins")
    public ApiResponseModel<List<Cabin>> getAvailableCabin(@RequestBody ApiRequestModelCabin cabinModel)
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
