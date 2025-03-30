package com.scm.majProj.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.scm.majProj.entities.User;
 import java.util.Optional;


public interface UserRepo extends JpaRepository<User, String> {
    // Custom query methods can be defined here if needed
    // For example, findByEmail, findByPhoneNumber, etc.

    Optional<User>findByEmail(String email);
    
    
}
