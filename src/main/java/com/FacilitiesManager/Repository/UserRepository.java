package com.FacilitiesManager.Repository;

import com.FacilitiesManager.Entity.BookingModel;
import com.FacilitiesManager.Entity.Enums.AccessRole;
import com.FacilitiesManager.Entity.Enums.UserApprovalStatus;
import com.FacilitiesManager.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface UserRepository extends JpaRepository<User,String> {

    @Query("SELECT u FROM User u WHERE  u.officeId=:officeId AND u.status=:status")
    List<User> findUserRequest(@Param("officeId") String officeId,@Param("status") UserApprovalStatus status);

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.officeId = :officeId")
    List<User> findUserByRoleAndOfficeId(@Param("role") AccessRole role, @Param("officeId") String officeId);

    @Query("SELECT u.emailId FROM User u WHERE u.role = :role AND u.officeId = :officeId")
    List<String> findEmailsByRoleAndOfficeId(@Param("role") AccessRole role, @Param("officeId") String officeId);
}
