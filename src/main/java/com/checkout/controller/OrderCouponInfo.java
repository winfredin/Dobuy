package com.checkout.controller;

public class OrderCouponInfo {
    private Integer couponNo;
    private Integer counterNo;

    // 預設建構子（必需的）
    public OrderCouponInfo() {
    }

    // Getters and Setters
    public Integer getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(Integer couponNo) {
        this.couponNo = couponNo;
    }

    public Integer getCounterNo() {
        return counterNo;
    }

    public void setCounterNo(Integer counterNo) {
        this.counterNo = counterNo;
    }



}
