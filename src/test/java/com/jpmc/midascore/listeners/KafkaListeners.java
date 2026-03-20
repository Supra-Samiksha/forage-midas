package com.jpmc.midascore.listeners;

// Import the Transaction class from its actual location
import com.jpmc.midascore.foundation.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners{

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core-group")
    public void listen(Transaction transaction) {

        System.out.println("Transaction received: " + transaction.getAmount());
    }
}