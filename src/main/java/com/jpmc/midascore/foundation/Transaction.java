package com.jpmc.midascore.foundation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {
    
    @JsonProperty("senderId")
    private long senderId;

    @JsonProperty("recipientId")
    private long recipientId;

    @JsonProperty("amount")
    private float amount;

    // Default constructor is REQUIRED for Jackson/Kafka deserialization
    public Transaction() {
    }

    public Transaction(long senderId, long recipientId, float amount) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
    }

    // Getters
    public long getSenderId() {
        return senderId;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public float getAmount() {
        return amount;
    }

    // Setters
    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public void setRecipientId(long recipientId) {
        this.recipientId = recipientId;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction {senderId=" + senderId + 
               ", recipientId=" + recipientId + 
               ", amount=" + amount + "}";
    }
}