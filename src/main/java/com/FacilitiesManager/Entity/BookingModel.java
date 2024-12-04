package com.FacilitiesManager.Entity;

import com.FacilitiesManager.Entity.Enums.BookingStatus;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name="booking_model")
public class BookingModel {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int sno;
        private int bookingId;
        private int cabinId;
        private String cabinName;
        private String userId;
        private String officeId;
        private String purpose;
        @Temporal(TemporalType.DATE)
        private Date date;
        private LocalTime validFrom;
        private LocalTime validTill;
          @Enumerated(EnumType.STRING)
        private BookingStatus status;

    public BookingModel(int sno, int bookingId, int cabinId, String userId, String officeId, String purpose, Date date, LocalTime validFrom, LocalTime validTill) {
        this.sno = sno;
        this.bookingId = bookingId;
        this.cabinId = cabinId;
        this.userId = userId;
        this.officeId = officeId;
        this.purpose = purpose;
        this.date = date;
        this.validFrom = validFrom;
        this.validTill = validTill;
    }

    public BookingModel() {
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getCabinName() {
        return cabinName;
    }

    public void setCabinName(String cabinName) {
        this.cabinName = cabinName;
    }

    @Override
    public String toString() {
        return "BookingModel{" +
                "sno=" + sno +
                ", bookingId=" + bookingId +
                ", cabinId=" + cabinId +
                ", userId='" + userId + '\'' +
                ", officeId='" + officeId + '\'' +
                ", purpose='" + purpose + '\'' +
                ", date=" + date +
                ", validFrom=" + validFrom +
                ", validTill=" + validTill +
                '}';
    }
}
