package com.counterorderdetail.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.counterHome.counterOrderDetailTest.model.NewCounterOrderDetailVO;
import com.counterorder.model.CounterOrderVO;

@Entity
@Table(name = "CounterOrderDetail")
public class CounterOrderDetailVO implements java.io.Serializable {
   private static final long serialVersionUID = 1L;

   private Integer counterOrderDetailNo;
   private Integer counterOrderNo;
   private Integer goodsNo;
   private Integer goodsNum;
   private Integer productPrice;
   private Integer productDisPrice;
   private Integer memCouponNo;
   private CounterOrderVO counterOrderVO;
   
   @ManyToOne
   @JoinColumn(name = "counterOrderNo", insertable = false, updatable = false)
   public CounterOrderVO getCounterOrderVO() {
	return counterOrderVO;
}

   public CounterOrderDetailVO(NewCounterOrderDetailVO newCounterOrderDetailVO) {
	   this.counterOrderNo = newCounterOrderDetailVO.getCounterOrder();
	   this.goodsNo = newCounterOrderDetailVO.getGoodsNo();
	   this.goodsNum = newCounterOrderDetailVO.getGoodsNum();
	   this.productPrice = newCounterOrderDetailVO.getGoodsPrice();
	   this.productDisPrice = newCounterOrderDetailVO.getGoodsNum() * newCounterOrderDetailVO.getGoodsPrice();
	   
   }
   
   
   
public void setCounterOrderVO(CounterOrderVO counterOrderVO) {
	this.counterOrderVO = counterOrderVO;
}

public CounterOrderDetailVO() {
   }

   @Id
   @Column(name = "counterOrderDetailNo")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   public Integer getCounterOrderDetailNo() {
       return this.counterOrderDetailNo;
   }

   public void setCounterOrderDetailNo(Integer counterOrderDetailNo) {
       this.counterOrderDetailNo = counterOrderDetailNo;
   }

   @Column(name = "counterOrderNo")
   @NotNull(message = "櫃台訂單編號: 請勿空白")
   @JoinColumn
   public Integer getCounterOrderNo() {
       return this.counterOrderNo;
   }

   public void setCounterOrderNo(Integer counterOrderNo) {
       this.counterOrderNo = counterOrderNo;
   }

   @Column(name = "goodsNo")
   @NotNull(message = "商品編號: 請勿空白")
   public Integer getGoodsNo() {
       return this.goodsNo;
   }

   public void setGoodsNo(Integer goodsNo) {
       this.goodsNo = goodsNo;
   }

   @Column(name = "goodsNum")
   @NotNull(message = "商品數量: 請勿空白")
   @Min(value = 1, message = "商品數量: 必須大於等於1")
   public Integer getGoodsNum() {
       return this.goodsNum;
   }

   public void setGoodsNum(Integer goodsNum) {
       this.goodsNum = goodsNum;
   }

   @Column(name = "productPrice")
   @NotNull(message = "商品價格: 請勿空白")
   @Min(value = 0, message = "商品價格: 必須大於等於0")
   public Integer getProductPrice() {
       return this.productPrice;
   }

   public void setProductPrice(Integer productPrice) {
       this.productPrice = productPrice;
   }
   public void setProductDisPrice(Integer productDisPrice) {
       this.productDisPrice = productDisPrice;
   }

   @Column(name = "productDisPrice")
   @NotNull(message = "商品折扣價: 請勿空白")
   @Min(value = 0, message = "商品折扣價: 必須大於等於0")
   public Integer getProductDisPrice() {
       return this.productDisPrice;
   }


   @Column(name = "memCouponNo")
   public Integer getMemCouponNo() {
       return this.memCouponNo;
   }

   public void setMemCouponNo(Integer memCouponNo) {
       this.memCouponNo = memCouponNo;
   }
}