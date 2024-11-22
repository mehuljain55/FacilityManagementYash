package com.FacilitiesManager.Entity;

import com.FacilitiesManager.Entity.Enums.BookingStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.util.Date;


@Entity
@Table(name="cabin_request")
public class CabinRequest {
    private  int requestId;
    private String cabinId;
    private String userId;
    private String purpose;
    private Date startDate;
    private Date endDate;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    public CabinRequest(int requestId, String cabinId, String userId, String purpose, Date startDate, Date endDate, BookingStatus status) {
        this.requestId = requestId;
        this.cabinId = cabinId;
        this.userId = userId;
        this.purpose = purpose;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public CabinRequest() {
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
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

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
