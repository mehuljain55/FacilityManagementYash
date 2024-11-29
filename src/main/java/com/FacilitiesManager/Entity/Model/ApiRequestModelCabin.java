package com.FacilitiesManager.Entity.Model;

import com.FacilitiesManager.Entity.Cabin;
import com.FacilitiesManager.Entity.User;

import java.util.List;

public class ApiRequestModelCabin {
    private String token;
    private User user;
    private List<Cabin> cabin;

    public ApiRequestModelCabin(String token, User user, List<Cabin> cabin) {
        this.token = token;
        this.user = user;
        this.cabin = cabin;
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

    public List<Cabin> getCabin() {
        return cabin;
    }

    public void setCabin(List<Cabin> cabin) {
        this.cabin = cabin;
    }
}
