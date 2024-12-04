package com.FacilitiesManager.Entity.Model;

import com.FacilitiesManager.Entity.CabinRequest;
import com.FacilitiesManager.Entity.User;

public class ApiRequestCabinModifyModel {

    private  String token;
    private User user;
    private  UserCabinModifyModel userCabinModifyModel;
    private CabinRequest cabinRequest;

    public ApiRequestCabinModifyModel(String token, User user, UserCabinModifyModel userCabinModifyModel, CabinRequest cabinRequest) {
        this.token = token;
        this.user = user;
        this.userCabinModifyModel = userCabinModifyModel;
        this.cabinRequest = cabinRequest;
    }

    public ApiRequestCabinModifyModel() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserCabinModifyModel getUserCabinModifyModel() {
        return userCabinModifyModel;
    }

    public void setUserCabinModifyModel(UserCabinModifyModel userCabinModifyModel) {
        this.userCabinModifyModel = userCabinModifyModel;
    }

    public CabinRequest getCabinRequest() {
        return cabinRequest;
    }

    public void setCabinRequest(CabinRequest cabinRequest) {
        this.cabinRequest = cabinRequest;
    }

    @Override
    public String toString() {
        return "ApiRequestCabinModifyModel{" +
                "token='" + token + '\'' +
                ", user=" + user +
                ", userCabinModifyModel=" + userCabinModifyModel +
                ", cabinRequest=" + cabinRequest +
                '}';
    }
}
