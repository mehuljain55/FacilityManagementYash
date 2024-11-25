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
      

}
