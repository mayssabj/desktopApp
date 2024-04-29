package com.codewarrior.markets_coupons.model;

import java.time.LocalDate;
import java.util.Date;

public class Voucher {
    private int id;
    private int categoryId;
    private int userWonId;
    private int marketRelatedId;
    private String code;
    private Date expiration;
    private double value;
    private int usageLimit;
    private String type;
    private boolean isValid;
    private boolean isGivenToUser;

    // Constructor
    public Voucher() {
    }

    public Voucher(double value, String code, Date selectedDate, int usable, boolean isValid, boolean isGiven, int marketId, int categoryId, int userId, String type) {
        this.value = value;
        this.code = code;
        this.expiration = selectedDate;
        this.usageLimit = usable;
        this.isValid = isValid;
        this.isGivenToUser = isGiven;
        this.marketRelatedId = marketId;
        this.categoryId = categoryId;
        this.userWonId = userId;
        this.type = type;
    }

    public Voucher(int id, double value, String code, Date selectedDate, int usable, boolean isValid, boolean isGiven, int marketId, int categoryId, int userId, String type) {
        this.value = value;
        this.code = code;
        this.expiration = selectedDate;
        this.usageLimit = usable;
        this.isValid = isValid;
        this.isGivenToUser = isGiven;
        this.marketRelatedId = marketId;
        this.categoryId = categoryId;
        this.userWonId = userId;
        this.type = type;
    }

    // Getters and setters for each property
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getUserWonId() {
        return userWonId;
    }

    public void setUserWonId(int userWonId) {
        this.userWonId = userWonId;
    }

    public int getMarketRelatedId() {
        return marketRelatedId;
    }

    public void setMarketRelatedId(int marketRelatedId) {
        this.marketRelatedId = marketRelatedId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(int usageLimit) {
        this.usageLimit = usageLimit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public boolean isGivenToUser() {
        return isGivenToUser;
    }

    public void setGivenToUser(boolean givenToUser) {
        isGivenToUser = givenToUser;
    }

    // toString method to display Voucher object as a string
    @Override
    public String toString() {
        return "Voucher{" +
                "id=" + id +
                ", code =" + code +
                ", categoryId=" + categoryId +
                ", userWonId=" + userWonId +
                ", marketRelatedId=" + marketRelatedId +
                ", code='" + code + '\'' +
                ", expiration=" + expiration +
                ", value=" + value +
                ", usageLimit=" + usageLimit +
                ", type='" + type + '\'' +
                ", isValid=" + isValid +
                ", isGivenToUser=" + isGivenToUser +
                '}';
    }
}