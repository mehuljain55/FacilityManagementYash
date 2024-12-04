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

public interface BookingModelRepository extends JpaRepository<BookingModel,Integer> {

    @Query("SELECT b FROM BookingModel b WHERE (b.validFrom <= :endTime AND b.validTill >= :startTime)  and b.date = :date" +
            " AND b.officeId=:officeId")
    List<BookingModel> findBookingsByOfficeIdBetweenTimes(@Param("officeId") String  officeId,
                                                 @Param("startTime") LocalTime startTime,
                                                 @Param("endTime") LocalTime endTime,
                                                 @Param(("date")) Date date);

    @Query("SELECT b FROM BookingModel b WHERE (b.validFrom <= :endTime AND b.validTill >= :startTime)" +
            "AND b.date=:date " +
            "AND b.cabinId=:cabinId " +
            "AND b.status=:status ")
    List<BookingModel> findBookingsByCabinIdSingleDayBetweenTimes( @Param("cabinId") int cabinId,
                                                               @Param("startTime") LocalTime startTime,
                                                               @Param("endTime") LocalTime endTime,
                                                               @Param(("date"))Date date,
                                                                @Param("status") BookingStatus status);


    @Query("SELECT b FROM BookingModel b WHERE (b.date <= :endDate AND b.date >= :startDate) AND b.cabinId=:cabinId AND b.status=:status")
    List<BookingModel> findBookingByCabinIdDate(@Param("startDate") Date startDate,
                                            @Param("endDate") Date endDate,
                                            @Param("cabinId") int cabinId,
                                            @Param("status") BookingStatus status);

    @Query("SELECT b FROM BookingModel b WHERE b.date <= :endDate AND b.date >= :startDate AND b.officeId=:officeId")
    List<BookingModel> findBookingsMultipleDaysBetweenDates(@Param("startDate") Date startDate,
                                                        @Param("endDate") Date endDate,
                                                        @Param("officeId") String officeId);

    @Query("SELECT b FROM BookingModel b WHERE b.bookingId=:bookingId")
    List<BookingModel> findByBookingId(@Param("bookingId") int bookingId);

    @Query("SELECT b FROM BookingModel b WHERE (b.validFrom <= :endTime AND b.validTill >= :startTime)  and b.date = :date" +
            " AND b.cabinId=:cabinId")
    List<BookingModel> findBookingsByCabinIdBetweenTimes(@Param("cabinId") int  cabinId,
                                                          @Param("startTime") LocalTime startTime,
                                                          @Param("endTime") LocalTime endTime,
                                                          @Param(("date")) Date date);

}
