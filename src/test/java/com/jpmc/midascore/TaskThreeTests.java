package com.jpmc.midascore;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.UserRecord;
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
public class TaskThreeTests {
    static final Logger logger = LoggerFactory.getLogger(TaskThreeTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private DatabaseConduit databaseConduit; // The tool to talk to the DB

    @Test
    void task_three_verifier() throws InterruptedException {
        // 1. Setup the data
        userPopulator.populate();
        
        // 2. Send the transactions to Kafka
        String[] transactionLines = fileLoader.loadStrings("/test_data/mnbvcxz.vbnm");
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }

        // 3. Wait for the background processing to finish
        logger.info("Processing transactions... please wait 20 seconds.");
        Thread.sleep(20000); 

        logger.info("---------------- SEARCHING FOR WALDORF ----------------");
        
        // 4. Look through the first 50 IDs in the database
        boolean found = false;
        for (long i = 1; i <= 50; i++) {
            UserRecord user = databaseConduit.findById(i);
            if (user != null && "waldorf".equalsIgnoreCase(user.getName())) {
                float balance = user.getBalance();
                int submissionNumber = (int) Math.floor(balance);
                
                System.out.println("******************************************");
                System.out.println("FOUND WALDORF!");
                System.out.println("RAW BALANCE: " + balance);
                System.out.println("YOUR SUBMISSION NUMBER: " + submissionNumber);
                System.out.println("******************************************");
                found = true;
                break; 
            }
        }

        if (!found) {
            System.out.println("ERROR: Waldorf was not found in the database. Check UserPopulator.");
        }
    }
}