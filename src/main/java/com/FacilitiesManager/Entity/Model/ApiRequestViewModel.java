package com.FacilitiesManager.Entity.Model;

import com.FacilitiesManager.Entity.CabinRequest;
import com.FacilitiesManager.Entity.Enums.BookingStatus;
import com.FacilitiesManager.Entity.User;
import jakarta.persistence.Enumerated;

public class ApiRequestViewModel {

    private String token;
    private User user;
    private BookingStatus status;
    private String officeId;

    public ApiRequestViewModel(String token, User user, String officeId) {
        this.token = token;
        this.user = user;
        this.officeId = officeId;
    }

    public ApiRequestViewModel() {
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

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
