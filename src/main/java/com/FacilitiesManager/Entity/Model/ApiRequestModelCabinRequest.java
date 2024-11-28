package com.FacilitiesManager.Entity.Model;

import com.FacilitiesManager.Entity.User;

public class ApiRequestModelCabinRequest {
    private String token;
    private User user;
    private CabinAvaliableModel cabinAvaliableModel;

    public ApiRequestModelCabinRequest(String token, User user, CabinAvaliableModel cabinAvaliableModel) {
        this.token = token;
        this.user = user;
        this.cabinAvaliableModel = cabinAvaliableModel;
    }

    public ApiRequestModelCabinRequest() {
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

    public CabinAvaliableModel getCabinAvaliableModel() {
        return cabinAvaliableModel;
    }

    public void setCabinAvaliableModel(CabinAvaliableModel cabinAvaliableModel) {
        this.cabinAvaliableModel = cabinAvaliableModel;
    }
}
