package com.FacilitiesManager.Entity;

import com.FacilitiesManager.Entity.Enums.AccessRole;
import com.FacilitiesManager.Entity.Enums.UserApprovalStatus;
import jakarta.persistence.*;

@Entity
@Table(name="user")
public class User {

    @Id
   private String emailId;
    @Enumerated(EnumType.STRING)
   private AccessRole role;
   private String name;
   private String mobileNo;
   private String password;
   private String officeId;
    @Enumerated(EnumType.STRING)
   private UserApprovalStatus status;


    public User(String emailId, AccessRole role, String name, String mobileNo, String password, String officeId, UserApprovalStatus status) {
        this.emailId = emailId;
        this.role = role;
        this.name = name;
        this.mobileNo = mobileNo;
        this.password = password;
        this.officeId = officeId;
        this.status = status;
    }

    public User() {
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public AccessRole getRole() {
        return role;
    }

    public void setRole(AccessRole role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public UserApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(UserApprovalStatus status) {
        this.status = status;
    }
}
