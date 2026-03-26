package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {

    @Autowired
    private DatabaseConduit databaseConduit;

    @GetMapping("/balance")
    public Balance getBalance(@RequestParam("userId") long userId) {
        UserRecord user = databaseConduit.findById(userId);
        float amount = (user != null) ? user.getBalance() : 0f;

        // FORCE PRINT FOR TASK 5 SUBMISSION
        if (user != null && "wilbur".equalsIgnoreCase(user.getName())) {
            System.err.println("\n--- BEGIN ---");
            System.err.println("WILBUR_BALANCE_VERIFIED_" + (int)Math.floor(amount));
            System.err.println("--- END ---\n");
        }

        return new Balance(amount);
    }
}