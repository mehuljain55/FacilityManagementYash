package com.FacilitiesManager.Repository;

import com.FacilitiesManager.Entity.CabinRequest;
import com.FacilitiesManager.Entity.Enums.BookingStatus;
import com.FacilitiesManager.Entity.Enums.OfficeStatus;
import com.FacilitiesManager.Entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfficeRepository extends JpaRepository<Office,String> {

    @Query("select o from Office o where o.status=:status")
    List<Office> findOfficeListByStatus(@Param("status")OfficeStatus status);

}
