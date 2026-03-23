package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.TransactionRecord;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.repository.TransactionRecordRepository; // Add this import
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository; // Add this field

    // Update constructor to include both
    public DatabaseConduit(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    public void save(TransactionRecord transactionRecord) {
        // Use the variable name (lowercase 't'), not the Class name
        transactionRecordRepository.save(transactionRecord);
    }

    // You will also need this helper method for your Listener
    public UserRecord findById(long id) {
        return userRepository.findById(id).orElse(null);
    }
    public Iterable<UserRecord> findAll() {
        return userRepository.findAll();
}
}