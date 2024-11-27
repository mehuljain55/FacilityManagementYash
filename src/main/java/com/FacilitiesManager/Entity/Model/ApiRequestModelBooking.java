package com.FacilitiesManager.Entity.Model;

import com.FacilitiesManager.Entity.CabinRequest;
import com.FacilitiesManager.Entity.User;


public class ApiRequestModelBooking {
    private String token;
    private CabinRequest cabinRequestModel;
    private User user;
    private String officeId;

    public ApiRequestModelBooking() {
    }

    public ApiRequestModelBooking(String token, CabinRequest cabinRequestModel, User user) {
        this.token = token;
        this.cabinRequestModel = cabinRequestModel;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public CabinRequest getCabinRequestModel() {
        return cabinRequestModel;
    }

    public void setCabinRequestModel(CabinRequest cabinRequestModel) {
        this.cabinRequestModel = cabinRequestModel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }
}
