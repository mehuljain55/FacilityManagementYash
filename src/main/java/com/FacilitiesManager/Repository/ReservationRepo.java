package com.FacilitiesManager.Repository;

import com.FacilitiesManager.Entity.Cabin;
import com.FacilitiesManager.Entity.CabinRequestModel;
import com.FacilitiesManager.Entity.Enums.BookingStatus;
import com.FacilitiesManager.Entity.Enums.BookingValadity;
import com.FacilitiesManager.Entity.ReservationList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public interface ReservationRepo extends JpaRepository<ReservationList,Integer> {


    @Query("SELECT r FROM ReservationList r WHERE (r.validFrom <= :endTime and r.validTill >= :startTime)  and r.date=:date"+
            " AND r.officeId=:officeId AND r.status=:status")
    List<ReservationList> findCabinReservationSingleDay(@Param("officeId") String  officeId,
                                                             @Param("startTime") LocalTime startTime,
                                                             @Param("endTime") LocalTime endTime,
                                                             @Param("date") Date date,
                                                             @Param("status") BookingStatus status);

}
