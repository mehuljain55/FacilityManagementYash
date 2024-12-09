package com.FacilitiesManager.Controller;


import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/office")
public class OfficeController {

    @Autowired
    private UserService userService;

    @GetMapping("/officeList")
    public ApiResponseModel getAllOfficeList()
    {
        return userService.findAllOffice();
    }

}
