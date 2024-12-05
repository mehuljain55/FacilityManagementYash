package com.FacilitiesManager.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="officeId")
public class Office {
    @Id
    private String officeId;
    private String officeName;
    private String address;

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
}
