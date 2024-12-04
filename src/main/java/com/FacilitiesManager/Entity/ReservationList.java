package com.FacilitiesManager.Entity;

import com.FacilitiesManager.Entity.Enums.BookingStatus;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name="reservation")
public class ReservationList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int reservationId;
    private int cabinId;
    private String userId;
    private Date date;
    private LocalTime validFrom;
    private LocalTime validTill;
    private String officeId;
    private BookingStatus status;

    public ReservationList(int reservationId, int cabinId, String userId, Date date, LocalTime validFrom, LocalTime validTill) {
        this.reservationId = reservationId;
        this.cabinId = cabinId;
        this.userId = userId;
        this.date = date;
        this.validFrom = validFrom;
        this.validTill = validTill;
    }

    public ReservationList() {
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
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
