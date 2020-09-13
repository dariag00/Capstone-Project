package com.kloso.capstoneproject.data.model;

import java.io.Serializable;
import java.util.Objects;

public class Participant implements Serializable {

    private String name;
    private String profilePictureUri;
    private boolean realUser;
    private String associatedUserId;
    private double netBalance;

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

    public String getProfilePictureUri() {
        return profilePictureUri;
    }

    public void setProfilePictureUri(String profilePictureUri) {
        this.profilePictureUri = profilePictureUri;
    }

    public void addBalance(double balance){
        this.netBalance+=balance;
    }

    public double getNetBalance() {
        return netBalance;
    }

    public void setNetBalance(double netBalance) {
        this.netBalance = netBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
