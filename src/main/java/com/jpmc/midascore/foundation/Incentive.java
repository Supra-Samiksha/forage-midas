package com.jpmc.midascore.foundation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Incentive {
    private float amount;

    // Default constructor is required for Jackson to deserialize the JSON
    public Incentive() {
    }

    public Incentive(float amount) {
        this.amount = amount;
    }

    // Getter
    public float getAmount() {
        return amount;
    }

    // Setter
    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Incentive{amount=" + amount + "}";
    }
}
