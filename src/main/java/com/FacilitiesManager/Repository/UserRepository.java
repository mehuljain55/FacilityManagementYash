package com.FacilitiesManager.Repository;

import com.FacilitiesManager.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
}
