package com.checkout.controller;

import java.util.Map;

public class CheckoutRequest {
    private Map<Integer, Integer> counterCoupons;

    // 預設建構子
    public CheckoutRequest() {
    }

    // Getter and Setter
    public Map<Integer, Integer> getCounterCoupons() {
        return counterCoupons;
    }

    public void setCounterCoupons(Map<Integer, Integer> counterCoupons) {
        this.counterCoupons = counterCoupons;
    }
}