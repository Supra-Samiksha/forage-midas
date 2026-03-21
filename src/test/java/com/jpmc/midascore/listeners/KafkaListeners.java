package com.jpmc.midascore.listeners;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.TransactionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @Autowired
    private DatabaseConduit databaseConduit;

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core-group")
    public void listen(Transaction transaction) {
        // 1. Fetch the actual User objects from the DB using the IDs in the Kafka message
        UserRecord sender = databaseConduit.findById(transaction.getSenderId());
        UserRecord recipient = databaseConduit.findById(transaction.getRecipientId());

        // 2. The Validation Rules
        if (sender != null && recipient != null && sender.getBalance() >= transaction.getAmount()) {
            
            // 3. Update the balances
            sender.setBalance(sender.getBalance() - transaction.getAmount());
            recipient.setBalance(recipient.getBalance() + transaction.getAmount());

            // 4. Save the updated users back to the database
            databaseConduit.save(sender);
            databaseConduit.save(recipient);

            // 5. Create and save the permanent record of this transaction
            TransactionRecord record = new TransactionRecord(sender, recipient, transaction.getAmount());
            databaseConduit.save(record);
        }
    }
}