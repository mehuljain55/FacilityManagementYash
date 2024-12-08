package com.FacilitiesManager.Entity.Model;

import com.FacilitiesManager.Entity.CabinRequest;
import com.FacilitiesManager.Entity.Enums.AccessRole;
import com.FacilitiesManager.Entity.User;

public class ApiRequestModel {

    private String token;
    private User user;
    private String officeId;
    private AccessRole accessRole;


    public ApiRequestModel(String token, User user, String officeId) {
        this.token = token;
        this.user = user;
        this.officeId = officeId;
    }

    public ApiRequestModel() {
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

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public AccessRole getAccessRole() {
        return accessRole;
    }

    public void setAccessRole(AccessRole accessRole) {
        this.accessRole = accessRole;
    }
}
