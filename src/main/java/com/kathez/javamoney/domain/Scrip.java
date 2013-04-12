package com.kathez.javamoney.domain;

import java.io.Serializable;
import javax.money.MonetaryAmount;

public class Scrip implements Serializable{
    
    private String scripId;
    private MonetaryAmount scripValue;    

    public Scrip() {
    }

    public Scrip(String scripId, MonetaryAmount scripValue) {
        this.scripId = scripId;
        this.scripValue = scripValue;       
    }   

    public String getScripId() {
        return scripId;
    }

    public void setScripId(String scripId) {
        this.scripId = scripId;
    }

    public MonetaryAmount getScripValue() {
        return scripValue;
    }

    public void setScripValue(MonetaryAmount scripValue) {
        this.scripValue = scripValue;
    }
}