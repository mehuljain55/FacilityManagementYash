package com.FacilitiesManager.Entity.Model;

public class DashboardModel {
    private int cabinRequestHold;
    private int cabinRequestApproved;
    private int cabinRequestRejected;
    private int userRequestPending;
    private int userRequestApproved;
    private int todaysCabinBooking;

    public DashboardModel(int cabinRequestHold, int cabinRequestApproved, int cabinRequestRejected, int userRequestPending, int userRequestApproved) {
        this.cabinRequestHold = cabinRequestHold;
        this.cabinRequestApproved = cabinRequestApproved;
        this.cabinRequestRejected = cabinRequestRejected;
        this.userRequestPending = userRequestPending;
        this.userRequestApproved = userRequestApproved;
    }

    public DashboardModel() {
    }

    public int getCabinRequestHold() {
        return cabinRequestHold;
    }

    public void setCabinRequestHold(int cabinRequestHold) {
        this.cabinRequestHold = cabinRequestHold;
    }

    public int getCabinRequestApproved() {
        return cabinRequestApproved;
    }

    public void setCabinRequestApproved(int cabinRequestApproved) {
        this.cabinRequestApproved = cabinRequestApproved;
    }

    public int getCabinRequestRejected() {
        return cabinRequestRejected;
    }

    public void setCabinRequestRejected(int cabinRequestRejected) {
        this.cabinRequestRejected = cabinRequestRejected;
    }

    public int getUserRequestPending() {
        return userRequestPending;
    }

    public void setUserRequestPending(int userRequestPending) {
        this.userRequestPending = userRequestPending;
    }

    public int getUserRequestApproved() {
        return userRequestApproved;
    }

    public void setUserRequestApproved(int userRequestApproved) {
        this.userRequestApproved = userRequestApproved;
    }

    public int getTodaysCabinBooking() {
        return todaysCabinBooking;
    }

    public void setTodaysCabinBooking(int todaysCabinBooking) {
        this.todaysCabinBooking = todaysCabinBooking;
    }
}
