package com.counterHome.cartTest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shoppingcartlist") // 對應資料表名稱
public class CartListVO {
	

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自動生成主鍵
	@Column(name = "shoppingCartListNo") // 資料表的欄位名稱
	private Integer shoppingCartListNo; // 購物車清單編號 (主鍵)
	
	@Column(name = "memNo", nullable = false) // 會員編號，不能為空
	private Integer memNo; // 會員編號
	
	@Column(name = "goodsNo", nullable = false) // 商品編號，不能為空
	private Integer goodsNo; // 商品編號
	
	@Column(name = "gpPhotos1") // 對應商品照片欄位
	private byte[] gpPhotos1; // 商品照片 (BLOB)
	
	@Column(name = "goodsName", length = 100, nullable = false) // 商品名稱，最多 100 字元，不能為空
	private String goodsName; // 商品名稱
	
	@Column(name = "goodsPrice", nullable = false) // 商品價格，不能為空
	private Integer goodsPrice; // 商品價格
	
	@Column(name = "goodsNum", nullable = false) // 商品數量，不能為空
	private Integer goodsNum; // 商品數量
	
	@Column(name = "orderTotalPrice", nullable = false) // 訂單總價，不能為空
	private Integer orderTotalPrice; // 訂單總價

	// Getters and Setters
	public Integer getShoppingCartListNo() {
		return shoppingCartListNo;
	}

	public void setShoppingCartListNo(Integer shoppingCartListNo) {
		this.shoppingCartListNo = shoppingCartListNo;
	}

	public Integer getMemNo() {
		return memNo;
	}

	public void setMemNo(Integer memNo) {
		this.memNo = memNo;
	}

	public Integer getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(Integer goodsNo) {
		this.goodsNo = goodsNo;
	}

	public byte[] getGpPhotos1() {
		return gpPhotos1;
	}

	public void setGpPhotos1(byte[] gpPhotos1) {
		this.gpPhotos1 = gpPhotos1;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Integer getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(Integer goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public Integer getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}

	public Integer getOrderTotalPrice() {
		return orderTotalPrice;
	}

	public void setOrderTotalPrice(Integer orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}

}
