package com.FacilitiesManager.Entity;

import com.FacilitiesManager.Entity.Enums.AccessRole;
import jakarta.persistence.*;

@Entity
@Table(name="user")
public class User {

    @Id
   private String emailId;
    @Enumerated(EnumType.STRING)
   private AccessRole role;
   private String name;
   private String password;
   private String officeId;



    public User(String emailId, AccessRole role, String name,String officeId) {
        this.emailId = emailId;
        this.role = role;
        this.name = name;
        this.officeId=officeId;
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
}
