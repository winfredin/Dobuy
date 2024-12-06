package com.ecpay.demo.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private  String productName;
    private  Integer price;
	public String getProductName() {
		// TODO Auto-generated method stub
		return this.productName;
	}
	public Integer getPrice() {
		// TODO Auto-generated method stub
		return this.price;
	}
}
