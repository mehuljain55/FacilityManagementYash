package com.FacilitiesManager.Entity.Model;

import com.FacilitiesManager.Entity.Office;
import com.FacilitiesManager.Entity.User;

import java.util.List;

public class ApiRequestOfficeAddModel {
    private User user;
    List<Office> offices;

    private String token;

    public ApiRequestOfficeAddModel(User user, List<Office> offices, String token) {
        this.user = user;
        this.offices = offices;
        this.token = token;
    }

    public ApiRequestOfficeAddModel() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Office> getOffices() {
        return offices;
    }

    public void setOffices(List<Office> offices) {
        this.offices = offices;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
