package com.FacilitiesManager.Repository;

import com.FacilitiesManager.Entity.BookingModel;
import com.FacilitiesManager.Entity.Cabin;
import com.FacilitiesManager.Entity.CabinRequest;
import com.FacilitiesManager.Entity.Enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public interface CabinRequestRepository extends JpaRepository<CabinRequest,Integer> {


    @Query("select c from CabinRequest c where c.status=:status and c.officeId=:officeId")
    List<CabinRequest> findCabinRequestByOfficeId(@Param("status") BookingStatus status,@Param("officeId") String officeId);

    @Query("select c from CabinRequest c where  c.userId=:userId")
    List<CabinRequest> findCabinRequestByUserId(@Param("userId") String userId);



}
