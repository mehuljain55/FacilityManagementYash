package com.FacilitiesManager.Controller;

import com.FacilitiesManager.Entity.Cabin;
import com.FacilitiesManager.Entity.Enums.AccessRole;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Model.ApiRequestModel;
import com.FacilitiesManager.Entity.Model.ApiRequestModelCabin;
import com.FacilitiesManager.Entity.Model.ApiRequestModelCabinRequest;
import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Service.CabinRequestService;
import com.FacilitiesManager.Service.UserAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cabin")
public class CabinController {

    @Autowired
    private CabinRequestService cabinRequestService;

    @Autowired
    private UserAuthorizationService userAuthorizationService;

    private final AccessRole accessRole=AccessRole.manager;

    
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

    @GetMapping("/download/format")
    public ResponseEntity<byte[]> downloadExcel() throws Exception {
        byte[] excelBytes = cabinRequestService.generateExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=cabin_details.xlsx");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelBytes);
    }

    @PostMapping("/deleteCabin")
    public ApiResponseModel deleteCabin(@RequestBody ApiRequestModelCabin apiRequestModelCabin)
    {
        boolean validateAccess=userAuthorizationService.validateUserAccess(apiRequestModelCabin.getUser(),apiRequestModelCabin.getToken(),accessRole);
        if(validateAccess)
        {
            ApiResponseModel apiResponseModel=cabinRequestService.deleteCabin(apiRequestModelCabin.getCabinId());
            return apiResponseModel;
        }else {
            return new ApiResponseModel(StatusResponse.unauthorized, null, "Unauthorized Access");
        }
    }
}
