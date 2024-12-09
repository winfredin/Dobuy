package com.goods.model;

import java.util.Base64;

import com.counter.model.CounterVO;
import com.goodstype.model.GoodsTypeVO;

public class GoodsLightVO {
	private Integer goodsNo;
	private String goodsName;
	private Integer goodsPrice;
	private Integer goodsAmount;
	private String base64Image;
	private GoodsTypeVO goodsTypeVO;
	private CounterVO counterVO;
	
	  public GoodsLightVO(GoodsVO goodsVO) {
	        if (goodsVO != null) {
	            this.goodsNo = goodsVO.getGoodsNo();
	            this.goodsName = goodsVO.getGoodsName();
	            this.goodsPrice = goodsVO.getGoodsPrice();
	            this.goodsAmount = goodsVO.getGoodsAmount();
	            if (goodsVO.getGpPhotos1() != null) {
	                this.base64Image = Base64.getEncoder().encodeToString(goodsVO.getGpPhotos1());
	            }
	            this.setGoodsTypeVO(goodsVO.getGoodsTypeVO());
	            this.setCounterVO(goodsVO.getCounterVO());
	        }
	    }
	
	public Integer getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(Integer goodsNo) {
		this.goodsNo = goodsNo;
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
	public Integer getGoodsAmount() {
		return goodsAmount;
	}
	public void setGoodsAmount(Integer goodsAmount) {
		this.goodsAmount = goodsAmount;
	}
	public String getBase64Image() {
		return base64Image;
	}
	public void setBase64Image(String base64Image) {
		this.base64Image = base64Image;
	}

	public GoodsTypeVO getGoodsTypeVO() {
		return goodsTypeVO;
	}

	public void setGoodsTypeVO(GoodsTypeVO goodsTypeVO) {
		this.goodsTypeVO = goodsTypeVO;
	}

	public CounterVO getCounterVO() {
		return counterVO;
	}

	public void setCounterVO(CounterVO counterVO) {
		this.counterVO = counterVO;
	}
}
