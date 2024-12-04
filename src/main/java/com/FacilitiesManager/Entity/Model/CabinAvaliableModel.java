package com.FacilitiesManager.Entity.Model;

import com.FacilitiesManager.Entity.Enums.BookingValadity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalTime;
import java.util.Date;

public class CabinAvaliableModel {
    private Date startDate;
    private Date endDate;
    private LocalTime validFrom;
    private LocalTime validTill;
    @Enumerated(EnumType.STRING)
    private BookingValadity bookingValadity;
    private String bookingType;
    private String officeId;
    private int cabinId;

    public CabinAvaliableModel(Date startDate, Date endDate, LocalTime validFrom, LocalTime validTill, BookingValadity bookingValadity) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.validFrom = validFrom;
        this.validTill = validTill;
        this.bookingValadity = bookingValadity;
    }

    public CabinAvaliableModel() {
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

    public BookingValadity getBookingValadity() {
        return bookingValadity;
    }

    public void setBookingValadity(BookingValadity bookingValadity) {
        this.bookingValadity = bookingValadity;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public int getCabinId() {
        return cabinId;
    }

    public void setCabinId(int cabinId) {
        this.cabinId = cabinId;
    }

    @Override
    public String toString() {
        return "CabinAvaliableModel{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", validFrom=" + validFrom +
                ", validTill=" + validTill +
                ", bookingValadity=" + bookingValadity +
                ", officeId='" + officeId + '\'' +
                '}';
    }
}
