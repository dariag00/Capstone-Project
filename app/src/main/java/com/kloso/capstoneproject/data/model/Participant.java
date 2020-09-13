package com.kloso.capstoneproject.data.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.math.BigDecimal;

public class Participant implements Serializable {

    private String name;
    private String profilePictureUri;
    private boolean realUser;
    private String associatedUserId;
    private String netBalance;

    public Participant(){
        this.netBalance = "0";
    }

    public Participant(String name){
        realUser = false;
        this.name = name;
        this.netBalance = "0";
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

    public void addBalance(BigDecimal balance){
        System.out.println("Añado: " + balance.toString());
        this.netBalance = new BigDecimal(this.netBalance).add(balance).toString();
    }

    public String getNetBalance() {
        return netBalance;
    }

    @Exclude
    public BigDecimal getNetBalanceBigDecimal(){
        return new BigDecimal(this.netBalance);
    }

    public void setNetBalance(String netBalance) {
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


    @Exclude
    public int compareTo(Participant participant){
        return this.getNetBalanceBigDecimal().compareTo(participant.getNetBalanceBigDecimal());
    }

}
