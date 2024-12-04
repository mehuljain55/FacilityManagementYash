package com.FacilitiesManager.Entity.Model;

import com.FacilitiesManager.Entity.Enums.BookingStatus;

public class UserCabinModifyModel {
    private  int  bookingId;
    private  int newCabinId;
    private BookingStatus status;

    public UserCabinModifyModel(int bookingId, int newCabinId, BookingStatus status) {
        this.bookingId = bookingId;
        this.newCabinId = newCabinId;
        this.status = status;
    }

    public UserCabinModifyModel() {
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getNewCabinId() {
        return newCabinId;
    }

    public void setNewCabinId(int newCabinId) {
        this.newCabinId = newCabinId;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
