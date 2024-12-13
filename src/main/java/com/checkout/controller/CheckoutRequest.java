package com.checkout.controller;

import java.util.Map;

public class CheckoutRequest {
    
    private String name;
    private String address;
    private String phone;
    private Integer counterNo;
    private String afterAmount;
    public String getAfterAmount() {
		return afterAmount;
	}

	public void setAfterAmount(String afterAmount) {
		this.afterAmount = afterAmount;
	}

	private Map<Integer, Integer> counterCoupons; // <CounterNo, CouponNo>

    // 預設建構子
    public CheckoutRequest() {
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getCounterNo() {
		return counterNo;
	}

	public void setCounterNo(Integer counterNo) {
		this.counterNo = counterNo;
	}

	// Getter and Setter
    public Map<Integer, Integer> getCounterCoupons() {
        return counterCoupons;
    }

    public void setCounterCoupons(Map<Integer, Integer> counterCoupons) {
        this.counterCoupons = counterCoupons;
    }
}