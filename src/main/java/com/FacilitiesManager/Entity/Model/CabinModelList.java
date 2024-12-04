package com.FacilitiesManager.Entity.Model;

import com.FacilitiesManager.Entity.Bookings;
import com.FacilitiesManager.Entity.Cabin;

import java.util.List;

public class CabinModelList {
    private Cabin cabin;
    private List<Bookings> bookings;

    public CabinModelList(Cabin cabin, List<Bookings> bookings) {
        this.cabin = cabin;
        this.bookings = bookings;
    }

    public CabinModelList() {
    }

    public Cabin getCabin() {
        return cabin;
    }

    public void setCabin(Cabin cabin) {
        this.cabin = cabin;
    }

    public List<Bookings> getBookings() {
        return bookings;
    }

    public void setBookings(List<Bookings> bookings) {
        this.bookings = bookings;
    }
}
