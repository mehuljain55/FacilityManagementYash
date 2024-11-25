package com.FacilitiesManager.Repository;

import com.FacilitiesManager.Entity.Bookings;
import com.FacilitiesManager.Entity.Cabin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface CabinRepository extends JpaRepository<Cabin,Integer> {

    @Query("select c from Cabin c where c.officeId=:officeId")
    public List<Cabin> findCabinByOfficeId(@Param("officeId") String officeId);

}
