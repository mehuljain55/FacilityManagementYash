package com.FacilitiesManager.Entity;

import com.FacilitiesManager.Entity.Enums.BookingStatus;
import com.FacilitiesManager.Entity.Enums.BookingValadity;
import com.FacilitiesManager.Entity.Enums.CabinAvaiability;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name="CabinRequestModel")
public class CabinRequestModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  int sno;
    private int cabinRequestId;
    private int cabinId;
    private String userId;
    private String purpose;
    private  String officeId;
    @Temporal(TemporalType.DATE)
    private Date date;
    private LocalTime validFrom;
    private LocalTime validTill;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public int getCabinRequestId() {
        return cabinRequestId;
    }

    public void setCabinRequestId(int cabinRequestId) {
        this.cabinRequestId = cabinRequestId;
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

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
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
}
