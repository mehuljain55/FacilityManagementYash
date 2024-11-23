package com.FacilitiesManager.Repository;

import com.FacilitiesManager.Entity.Cabin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CabinRepository extends JpaRepository<Cabin,Integer> {
}
