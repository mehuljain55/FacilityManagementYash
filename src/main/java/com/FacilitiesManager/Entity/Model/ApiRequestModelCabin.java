package com.FacilitiesManager.Entity.Model;

import com.FacilitiesManager.Entity.CabinRequest;
import com.FacilitiesManager.Entity.User;

public class ApiRequestModelCabin {
    private String token;
    private User user;
    private CabinAvaliableModel cabinAvaliableModel;

    public ApiRequestModelCabin(String token, User user, CabinAvaliableModel cabinAvaliableModel) {
        this.token = token;
        this.user = user;
        this.cabinAvaliableModel = cabinAvaliableModel;
    }

    public ApiRequestModelCabin() {
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
