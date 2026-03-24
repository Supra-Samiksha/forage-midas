package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IncentiveServiceClient {
    private final RestTemplate restTemplate;
    private final String url = "http://localhost:8080/incentive";

    public IncentiveServiceClient() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Sends a POST request to the Incentive API with the transaction data.
     * The API responds with an Incentive object containing the reward amount.
     */
    public Incentive getIncentive(Transaction transaction) {
        try {
            return restTemplate.postForObject(url, transaction, Incentive.class);
        } catch (Exception e) {
            // If the external API is down, we return an incentive of 0
            // so the transaction can still complete without a bonus.
            return new Incentive(0f);
        }
    }
}
