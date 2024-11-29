package com.FacilitiesManager.Entity.Model;

import com.FacilitiesManager.Entity.CabinRequest;
import com.FacilitiesManager.Entity.User;

public class ApiRequestUserModel {
    private String token;
    private String employeeId;
    private User user;

    public ApiRequestUserModel(String token, String employeeId, User user) {
        this.token = token;
        this.employeeId = employeeId;
        this.user = user;
    }

    public ApiRequestUserModel() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
