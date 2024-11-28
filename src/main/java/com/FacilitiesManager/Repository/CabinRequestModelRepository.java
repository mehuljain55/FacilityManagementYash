package com.FacilitiesManager.Repository;

import com.FacilitiesManager.Entity.CabinRequest;
import com.FacilitiesManager.Entity.CabinRequestModel;
import com.FacilitiesManager.Entity.Enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public interface CabinRequestModelRepository extends JpaRepository<CabinRequestModel,Integer> {

    @Query("SELECT c FROM CabinRequestModel c WHERE (c.validFrom > :endTime and c.validTill < :startTime)  and c.date=:date"+
            " AND c.officeId=:officeId")
    List<CabinRequestModel> findCabinBookingRequestSingleDay(@Param("officeId") String  officeId,
                                                             @Param("startTime") LocalTime startTime,
                                                             @Param("endTime") LocalTime endTime,
                                                             @Param("date") Date date,
                                                             @Param("status")BookingStatus status);

    @Query("SELECT  c FROM CabinRequestModel c WHERE c.date <= :startDate AND c.date >= :endDate AND c.officeId=:officeId")
    List<CabinRequestModel> findCabinBookingRequestMultipleDay(@Param("startDate") Date startDate,
                                                          @Param("endDate") Date endDate,
                                                          @Param("officeId") String officeId,
                                                          @Param("status")BookingStatus status);
}
