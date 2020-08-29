package com.kloso.capstoneproject.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExpenseGroup implements Serializable {

    private String name;
    private String description;
    private List<Participant> participants;


    public ExpenseGroup(){
        participants = new ArrayList<>();
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

}
