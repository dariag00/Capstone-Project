package com.kloso.capstoneproject.data.model;

import com.mynameismidori.currencypicker.ExtendedCurrency;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExpenseGroup implements Serializable {

    private String id;
    private String name;
    private String description;
    private List<Participant> participants;
    private ExtendedCurrency currency;
    private List<String> associatedUsers;


    public ExpenseGroup(){
        participants = new ArrayList<>();
        associatedUsers = new ArrayList<>();
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

    public ExtendedCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(ExtendedCurrency currency) {
        this.currency = currency;
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

    public void addUser(String email){
        this.associatedUsers.add(email);
    }

}
