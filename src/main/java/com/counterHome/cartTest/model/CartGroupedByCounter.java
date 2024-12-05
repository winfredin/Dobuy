package com.counterHome.cartTest.model;


//用櫃位編號來分類購物車清單的VO
public class CartGroupedByCounter {
	private int counterNo;
	private CartListVO cartListVO;
	
	public int getCounterNo() {
		return counterNo;
	}
	public void setCounterNo(int counterNo) {
		this.counterNo = counterNo;
	}
	public CartListVO getCartListVO() {
		return cartListVO;
	}
	public void setCartListVO(CartListVO cartListVO) {
		this.cartListVO = cartListVO;
	}
}
