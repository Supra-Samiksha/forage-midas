package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.UserRecord;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserRecord, Long> {
    // DO NOT add findById here; CrudRepository already has it!
    
    // Add this so you can look up "waldorf" by name later
    UserRecord findByName(String name);
}