package com.FacilitiesManager.Entity;

import com.FacilitiesManager.Entity.Enums.BookingValadity;
import com.FacilitiesManager.Entity.Enums.CabinAvaiability;
import jakarta.persistence.*;

@Entity
@Table(name="cabin")
public class Cabin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cabinId;
    private String cabinName;
    private int capacity;
    private String appliances;
    private String officeId;
    @Enumerated(EnumType.STRING)
    private BookingValadity bookingType;
    @Enumerated(EnumType.STRING)
    private CabinAvaiability status;
    private String msg;

    public Cabin() {
    }

    public int getCabinId() {
        return cabinId;
    }

    public void setCabinId(int cabinId) {
        this.cabinId = cabinId;
    }

    public String getCabinName() {
        return cabinName;
    }

    public void setCabinName(String cabinName) {
        this.cabinName = cabinName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public CabinAvaiability getStatus() {
        return status;
    }

    public void setStatus(CabinAvaiability status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BookingValadity getBookingType() {
        return bookingType;
    }

    public void setBookingType(BookingValadity bookingType) {
        this.bookingType = bookingType;
    }

    public String getAppliances() {
        return appliances;
    }

    public void setAppliances(String appliances) {
        this.appliances = appliances;
    }
}