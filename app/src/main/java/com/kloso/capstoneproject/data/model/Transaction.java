package com.kloso.capstoneproject.data.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Transaction implements Serializable {

    private BigDecimal balance;
    private Participant payer;
    private Participant receiver;

    public Transaction() {
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
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
