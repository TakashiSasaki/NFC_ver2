package com.gmail.matsushige.nfcv2.db;

/**
 * Created by sasaki on 13/09/29.
 */
public class TemporaryUser {
    String cardType;
    String cardId;
    String ownerName;
    String registrationCode;

    public TemporaryUser(String card_type, String card_id){
        this.cardId = card_id;
        this.cardType = card_type;
    }

    public void setOwnerName(String owner_name){
        this.ownerName = owner_name;
    }

    public void setRegistrationCode(String registration_code){
        this.registrationCode = registration_code;
    }
    public String getOwnerName(){
        return this.ownerName;
    }

    public String getRegistrationCode(){
        return this.registrationCode;
    }
}//TemporaryUser
