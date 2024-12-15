package com.counterHome.counterOrderDetailTest.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.counterHome.cartTest.model.CartListVO;

@Entity
@Table(name = "newcounterorderdetail")
public class NewCounterOrderDetailVO{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "counterOrderDetailNo")
    private Integer counterOrderDetailNo; // 明細主鍵


    @Column(name = "cOrderNo", nullable = false) 
    private Integer counterOrder; // 櫃位訂單

    
    @Column(name = "goodsNo", nullable = false)
    private Integer goodsNo; // 商品

    @Column(name = "goodsNum", nullable = false)
    private Integer goodsNum; // 商品數量

    @Column(name = "goodsPrice", nullable = false)
    private Integer goodsPrice; // 商品單價

    
    public NewCounterOrderDetailVO(CartListVO cartListVO) {
    	this.goodsNo = cartListVO.getGoodsNo();
    	this.goodsNum = cartListVO.getGoodsNum();
    	this.goodsPrice = cartListVO.getGoodsPrice();
    	
    	
    }
    
    
    // Getter 和 Setter 方法
    public Integer getCounterOrderDetailNo() {
        return counterOrderDetailNo;
    }

    public void setCounterOrderDetailNo(Integer counterOrderDetailNo) {
        this.counterOrderDetailNo = counterOrderDetailNo;
    }

   
    public Integer getCounterOrder() {
		return counterOrder;
	}

	public void setCounterOrder(Integer counterOrder) {
		this.counterOrder = counterOrder;
	}

	public Integer getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(Integer goodsNo) {
		this.goodsNo = goodsNo;
	}

	public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Integer getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Integer goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

}
