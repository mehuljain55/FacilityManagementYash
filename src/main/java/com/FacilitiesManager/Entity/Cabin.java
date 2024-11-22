package com.FacilitiesManager.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="cabin")
public class Cabin {
    private int cabinId;
    private String cabinName;
    private int capacity;
    private String officeId;

    public Cabin(int cabinId, String cabinName, int capacity, String officeId) {
        this.cabinId = cabinId;
        this.cabinName = cabinName;
        this.capacity = capacity;
        this.officeId = officeId;
    }

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
}
