package com.FacilitiesManager.Repository;

import com.FacilitiesManager.Entity.BookingModel;
import com.FacilitiesManager.Entity.Bookings;
import com.FacilitiesManager.Entity.Enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public interface BookingRepository extends JpaRepository<Bookings,Integer> {

    @Query("SELECT b FROM Bookings b WHERE b.officeId=:officeId")
    List<Bookings> findBookingsByOfficeId(@Param("officeId") String  officeId);

    @Query("SELECT b FROM Bookings b WHERE (b.startDate <= :endDate AND b.endDate >= :startDate) AND b.cabinId=:cabinId AND b.status=:status")
    List<Bookings> findBookingByCabinId(@Param("startDate") Date startDate,
                                                @Param("endDate") Date endDate,
                                                @Param("cabinId") int cabinId,
                                                @Param("status") BookingStatus status);

}
