package com.jpmc.midascore;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class TaskFourTests {
    static final Logger logger = LoggerFactory.getLogger(TaskFourTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

   @Autowired
    private com.jpmc.midascore.component.DatabaseConduit databaseConduit;

    @Test
    void task_four_verifier() throws InterruptedException {
        // 1. Setup Data
        userPopulator.populate();
        
        // 2. Send Transactions (The new file for Task 4)
        String[] transactionLines = fileLoader.loadStrings("/test_data/alskdjfh.fhdjsk");
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }

        // 3. Wait for Kafka and the External API to finish
        logger.info("Processing Task 4 transactions... waiting 20 seconds.");
        Thread.sleep(20000); 

        // 4. Find Wilbur and Print the Answer
        System.err.println("##########################################");
        boolean found = false;
        // Search through IDs to find Wilbur
        for (long i = 1; i <= 100; i++) {
            com.jpmc.midascore.entity.UserRecord user = databaseConduit.findById(i);
            if (user != null && "wilbur".equalsIgnoreCase(user.getName())) {
                float balance = user.getBalance();
                int submissionCode = (int) Math.floor(balance);
                
                System.err.println("!!! TASK 4 RESULT !!!");
                System.err.println("WILBUR FINAL BALANCE: " + balance);
                System.err.println("YOUR SUBMISSION CODE: " + submissionCode);
                found = true;
                break;
            }
        }

        if (!found) {
            System.err.println("ERROR: Wilbur not found in DB.");
        }
        System.err.println("##########################################");
        
        // No more while(true) - the test will now finish on its own!
    }
}
