package com.FacilitiesManager.Entity;

import com.FacilitiesManager.Entity.Enums.OfficeStatus;
import jakarta.persistence.*;

@Entity
@Table(name="officeId")
public class Office {
    @Id
    private String officeId;
    private String officeName;
    private String address;
    @Enumerated(EnumType.STRING)
    private OfficeStatus status;

    public Office() {
    }

    public Office(String officeId, String officeName, String address) {
        this.officeId = officeId;
        this.officeName = officeName;
        this.address = address;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OfficeStatus getStatus() {
        return status;
    }

    public void setStatus(OfficeStatus status) {
        this.status = status;
    }
}
