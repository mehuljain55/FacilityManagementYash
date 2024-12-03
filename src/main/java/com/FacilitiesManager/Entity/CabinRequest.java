package com.FacilitiesManager.Entity;

import com.FacilitiesManager.Entity.Enums.BookingStatus;
import com.FacilitiesManager.Entity.Enums.BookingValadity;
import com.FacilitiesManager.Entity.Enums.CabinAvaiability;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.Date;


@Entity
@Table(name="cabin_request")
public class CabinRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  int requestId;
    private int cabinId;
    private String cabinName;
    private String userId;
    private String purpose;
    private  String officeId;

    @Temporal(TemporalType.DATE)
    private Date requestDate;

    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;
    private LocalTime validFrom;
    private LocalTime validTill;

    @Enumerated(EnumType.STRING)
    private BookingValadity bookingValadity;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    @Enumerated(EnumType.STRING)
    private CabinAvaiability cabinAvaiability;

    public CabinRequest(int requestId, int cabinId, String userId, String purpose, Date startDate, Date endDate, LocalTime validFrom, LocalTime validTill, BookingValadity bookingValadity, BookingStatus status) {
        this.requestId = requestId;
        this.cabinId = cabinId;
        this.userId = userId;
        this.purpose = purpose;
        this.startDate = startDate;
        this.endDate = endDate;
        this.validFrom = validFrom;
        this.validTill = validTill;
        this.bookingValadity = bookingValadity;
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

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public CabinAvaiability getCabinAvaiability() {
        return cabinAvaiability;
    }

    public void setCabinAvaiability(CabinAvaiability cabinAvaiability) {
        this.cabinAvaiability = cabinAvaiability;
    }

    public String getCabinName() {
        return cabinName;
    }

    public void setCabinName(String cabinName) {
        this.cabinName = cabinName;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }
}
