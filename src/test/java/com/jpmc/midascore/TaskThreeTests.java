package com.jpmc.midascore;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.UserRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TaskThreeTests {

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private DatabaseConduit databaseConduit;

    @Test
    void task_three_verifier() {
        // 1. Load the initial users into the DB
        userPopulator.populate();

        // 2. Load the transaction data from the file
        String[] lines = fileLoader.loadStrings("/test_data/mnbvcxz.vbnm");

        // 3. Process each line manually (Doing exactly what the Listener should do)
        for (String line : lines) {
            String[] data = line.split(", ");
            long senderId = Long.parseLong(data[0]);
            long recipientId = Long.parseLong(data[1]);
            float amount = Float.parseFloat(data[2]);

            UserRecord sender = databaseConduit.findById(senderId);
            UserRecord recipient = databaseConduit.findById(recipientId);

            if (sender != null && recipient != null && sender.getBalance() >= amount) {
                sender.setBalance(sender.getBalance() - amount);
                recipient.setBalance(recipient.getBalance() + amount);
                databaseConduit.save(sender);
                databaseConduit.save(recipient);
            }
        }

        // 4. THE FINAL SCAN
        System.err.println("##########################################");
        
        Iterable<UserRecord> allUsers = databaseConduit.findAll();
        boolean found = false;

        for (UserRecord user : allUsers) {
            if (user != null && "waldorf".equalsIgnoreCase(user.getName())) {
                float finalBalance = user.getBalance();
                int resultNumber = (int) Math.floor(finalBalance);
                
                System.err.println("!!! FOUND WALDORF !!!");
                System.err.println("FINAL BALANCE: " + finalBalance);
                System.err.println("SUBMISSION CODE: " + resultNumber);
                found = true;
                break;
            }
        }

        if (!found) {
            System.err.println("ERROR: Waldorf still not found in Database.");
        }
        System.err.println("##########################################");
    }
}