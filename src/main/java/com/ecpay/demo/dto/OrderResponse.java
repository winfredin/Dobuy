package com.ecpay.demo.dto;

import lombok.Data;

@Data
public class OrderResponse {
    private Integer orderId;
    private  String form;
	public void setOrderId(Integer orderId) {
		this.orderId=orderId;
		
	}
	public void setForm(String form) {
		this.form=form;
		
	}
}
