package com.kloso.capstoneproject.data.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExpenseGroup implements Serializable {

    private String id;
    private String name;
    private String description;
    private List<Participant> participants;
    private String currencyCode;
    private List<String> associatedUsers;
    private List<Expense> expenseList;


    public ExpenseGroup(){
        participants = new ArrayList<>();
        associatedUsers = new ArrayList<>();
        expenseList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public void addParticipant(Participant participant){
        participants.add(participant);
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getAssociatedUsers() {
        return associatedUsers;
    }

    public void setAssociatedUsers(List<String> associatedUsers) {
        this.associatedUsers = associatedUsers;
    }

    public List<Expense> getExpenseList() {
        return expenseList;
    }

    public void setExpenseList(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    public void addUser(String email){
        this.associatedUsers.add(email);
    }

    public void addExpense(Expense expense){
        this.expenseList.add(expense);
    }

    public Participant getParticipantByName(String name){
        Participant obtainedParticipant = null;
        for(Participant participant : participants){
            if(participant.getName().equals(name))
                obtainedParticipant = participant;
        }

        return obtainedParticipant;
    }

}
