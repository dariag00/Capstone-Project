package com.kloso.capstoneproject.data.model;

import java.io.Serializable;

public class Participant implements Serializable {

    private String name;

    public Participant(String name){
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
