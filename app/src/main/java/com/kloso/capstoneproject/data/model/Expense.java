package com.kloso.capstoneproject.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Expense implements Serializable {

    private Participant paidBy;
    private List<Participant> paidFor;
    private double amount;
    private String expenseName;
    private Date expenseDate;
    private boolean isProcessed;

    public Expense(){
        isProcessed = false;
    }

    public Participant getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(Participant paidBy) {
        this.paidBy = paidBy;
    }

    public List<Participant> getPaidFor() {
        return paidFor;
    }

    public void setPaidFor(List<Participant> paidFor) {
        this.paidFor = paidFor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }
}
