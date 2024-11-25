package com.FacilitiesManager.Repository;

import com.FacilitiesManager.Entity.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public interface BookingRepository extends JpaRepository<Bookings,Integer> {

      @Query("select b from Bookings b where b.officeId=:officeId and b.startDate<=:date and b.endDate>=:date")
      public List<Bookings> getBookingByDateandOffice(String officeId, Date date);

      @Query("SELECT b FROM Bookings b WHERE (b.validFrom <= :startTime AND b.validTill >= :endTime) " +
              "AND b.startDate=:startDate" +
              " AND b.cabinId=:cabinId")
      List<Bookings> findBookingsByCabinIdSingleDayBetweenTimes( @Param("cabinId") int cabinId,
                                                        @Param("startTime") LocalTime startTime,
                                                        @Param("endTime") LocalTime endTime,
                                                        @Param(("startDate"))Date startDate);


      @Query("SELECT b FROM Bookings b WHERE b.startDate <= :startDate AND b.endDate >= :endDate AND b.cabinId=:cabinId")
      List<Bookings> findBookingsBetweenDates(@Param("startDate") Date startDate,
                                              @Param("endDate") Date endDate,
                                              @Param("cabinId") int cabinId);

      @Query("SELECT b FROM Bookings b WHERE (b.validFrom <= :startTime AND b.validTill >= :endTime) " +
              "AND (b.startDate <= :startDate AND b.endDate >= :startDate)" +
              " AND b.officeId=:officeId")
      List<Bookings> findBookingsBySingleDayBetweenTimes( @Param("officeId") String  officeId,
                                                                 @Param("startTime") LocalTime startTime,
                                                                 @Param("endTime") LocalTime endTime,
                                                                 @Param(("startDate"))Date startDate);

      @Query("SELECT b FROM Bookings b WHERE b.startDate <= :startDate AND b.endDate >= :endDate AND b.officeId=:officeId")
      List<Bookings> findBookingsMultipleDaysBetweenDates(@Param("startDate") Date startDate,
                                              @Param("endDate") Date endDate,
                                              @Param("officeId") String officeId);


}
