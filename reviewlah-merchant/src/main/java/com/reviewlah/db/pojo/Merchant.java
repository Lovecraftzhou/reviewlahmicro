package com.reviewlah.db.pojo;

import java.math.BigInteger;

public class Merchant {
    private BigInteger merchant_id;
    private String name;
    private String phone_number;
    private String email;
    private String password;
    private String avator;
    private double avg_rate;

    public BigInteger getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(BigInteger merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public double getAvg_rate() {
        return avg_rate;
    }

    public void setAvg_rate(double avg_rate) {
        this.avg_rate = avg_rate;
    }
}
