package com.FacilitiesManager.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;


@Entity
@Table(name="booking")
public class Bookings {
    @Id
    private  int bookingId;
    private String cabinId;
    private String userId;
    private String officeId;
    private String purpose;
    private Date startDate;
    private Date endDate;


    public Bookings(int bookingId, String cabinId, String userId, String purpose, Date startDate, Date endDate) {
        this.bookingId = bookingId;
        this.cabinId = cabinId;
        this.userId = userId;
        this.purpose = purpose;
        this.startDate = startDate;
        this.endDate = endDate;
    }



    public Bookings() {
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getCabinId() {
        return cabinId;
    }

    public void setCabinId(String cabinId) {
        this.cabinId = cabinId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
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
