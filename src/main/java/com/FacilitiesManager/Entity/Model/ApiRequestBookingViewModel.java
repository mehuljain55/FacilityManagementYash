package com.FacilitiesManager.Entity.Model;

import com.FacilitiesManager.Entity.User;

import java.util.Date;

public class ApiRequestBookingViewModel {

    private String token;
    private User user;
    private String officeId;
    private Date startDate;
    private Date endDate;

    public ApiRequestBookingViewModel() {
    }

    public ApiRequestBookingViewModel(String token, User user, String officeId, Date startDate, Date endDate) {
        this.token = token;
        this.user = user;
        this.officeId = officeId;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}

