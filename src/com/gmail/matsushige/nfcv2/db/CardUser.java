package com.gmail.matsushige.nfcv2.db;

/**
 * Created by sasaki on 13/09/29.
 */
public class CardUser {
    String cardType;
    String cardId;
    String ownerName;
    String registrationCode;

    public CardUser(String card_type, String card_id) {
        this.cardId = card_id;
        this.cardType = card_type;
    }

    public String getCardType() {
        return this.cardType;
    }

    public String getCardId() {
        return this.cardId;
    }

    public void setOwnerName(String owner_name) {
        this.ownerName = owner_name;
    }

    public void setRegistrationCode(String registration_code) {
        this.registrationCode = registration_code;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public String getRegistrationCode() {
        return this.registrationCode;
    }
}//CardUser
