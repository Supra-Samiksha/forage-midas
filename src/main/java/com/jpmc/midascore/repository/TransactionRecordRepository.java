package com.jpmc.midascore.repository;

import com.jpmc.midascore.foundation.TransactionRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRecordRepository extends CrudRepository<TransactionRecord, Long> {
    // Spring will automatically implement the save(), findById(), etc. methods for you!
}