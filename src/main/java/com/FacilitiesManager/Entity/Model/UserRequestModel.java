package com.FacilitiesManager.Entity.Model;

import com.FacilitiesManager.Entity.User;

import java.util.List;

public class UserRequestModel {
    private String token;
    private  User user;
    private List<User> userList;

    public UserRequestModel(String token, User user, List<User> userList) {
        this.token = token;
        this.user = user;
        this.userList = userList;
    }

    public UserRequestModel() {
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

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
