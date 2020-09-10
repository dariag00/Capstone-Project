package com.kloso.capstoneproject.data.model;

import java.io.Serializable;

public class Participant implements Serializable {

    private String name;
    private boolean realUser;
    private String associatedUserId;

    public Participant(){}

    public Participant(String name){
        realUser = false;
        this.name = name;
    }

    public boolean isRealUser() {
        return realUser;
    }

    public void setRealUser(boolean realUser) {
        this.realUser = realUser;
    }

    public String getAssociatedUserId() {
        return associatedUserId;
    }

    public void setAssociatedUserId(String associatedUserId) {
        this.associatedUserId = associatedUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
