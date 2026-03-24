package com.jpmc.midascore.listeners;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.component.IncentiveServiceClient;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.TransactionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @Autowired
    private DatabaseConduit databaseConduit;

    @Autowired
    private IncentiveServiceClient incentiveServiceClient;

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core-group")
    public void listen(Transaction transaction) {
        // 1. Fetch users from database
        UserRecord sender = databaseConduit.findById(transaction.getSenderId());
        UserRecord recipient = databaseConduit.findById(transaction.getRecipientId());

        // 2. Validation: Ensure both exist and sender has enough funds
        if (sender != null && recipient != null && sender.getBalance() >= transaction.getAmount()) {
            
            // 3. External API Call: Get the incentive amount
            Incentive incentive = incentiveServiceClient.getIncentive(transaction);
            float rewardAmount = (incentive != null) ? incentive.getAmount() : 0f;

            // 4. Update Balances
            // Sender pays only the transaction amount
            sender.setBalance(sender.getBalance() - transaction.getAmount());
            
            // Recipient gets the transaction amount + the external incentive
            recipient.setBalance(recipient.getBalance() + transaction.getAmount() + rewardAmount);

            // 5. Persist Changes
            databaseConduit.save(sender);
            databaseConduit.save(recipient);

            // 6. Record the transaction (Include the incentive in your record if your entity supports it)
            TransactionRecord record = new TransactionRecord(sender, recipient, transaction.getAmount());
            // Note: If you added an incentive field to TransactionRecord in Task 4, set it here.
            databaseConduit.save(record);
        }
    }
}