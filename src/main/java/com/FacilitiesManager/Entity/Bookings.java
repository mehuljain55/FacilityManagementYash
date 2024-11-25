package com.FacilitiesManager.Entity;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.Date;


@Entity
@Table(name="booking")
public class Bookings {
    @Id
    private  int bookingId;
    private int cabinId;
    private String userId;
    private String officeId;
    private String purpose;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;
    private LocalTime validFrom;
    private LocalTime validTill;


    public Bookings(int bookingId, int cabinId, String userId, String officeId, String purpose, Date startDate, Date endDate, LocalTime validFrom, LocalTime validTill) {
        this.bookingId = bookingId;
        this.cabinId = cabinId;
        this.userId = userId;
        this.officeId = officeId;
        this.purpose = purpose;
        this.startDate = startDate;
        this.endDate = endDate;
        this.validFrom = validFrom;
        this.validTill = validTill;
    }

    public Bookings() {
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getCabinId() {
        return cabinId;
    }

    public void setCabinId(int cabinId) {
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

    public LocalTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalTime getValidTill() {
        return validTill;
    }

    public void setValidTill(LocalTime validTill) {
        this.validTill = validTill;
    }

    @Override
    public String toString() {
        return "Bookings{" +
                "bookingId=" + bookingId +
                ", cabinId=" + cabinId +
                ", userId='" + userId + '\'' +
                ", officeId='" + officeId + '\'' +
                ", purpose='" + purpose + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", validFrom=" + validFrom +
                ", validTill=" + validTill +
                '}';
    }
}
