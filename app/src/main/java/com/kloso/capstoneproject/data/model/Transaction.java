package com.kloso.capstoneproject.data.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.math.BigDecimal;

public class Transaction implements Serializable {

    private String balance;
    private Participant payer;
    private Participant receiver;

    public Transaction() {
    }

    @Exclude
    public BigDecimal getBalanceBigDecimal() {
        return new BigDecimal(balance);
    }

    public String getBalance(){
        return this.balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Participant getPayer() {
        return payer;
    }

    public void setPayer(Participant payer) {
        this.payer = payer;
    }

    public Participant getReceiver() {
        return receiver;
    }

    public void setReceiver(Participant receiver) {
        this.receiver = receiver;
    }
}
