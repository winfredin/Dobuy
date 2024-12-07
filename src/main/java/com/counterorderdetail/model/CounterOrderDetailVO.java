package com.counterorderdetail.model;

import javax.persistence.*;
import javax.validation.constraints.*;

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
   private String productSpec;
   private Integer memCouponNo;

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

   @Column(name = "productDisPrice")
   @NotNull(message = "商品折扣價: 請勿空白")
   @Min(value = 0, message = "商品折扣價: 必須大於等於0")
   public Integer getProductDisPrice() {
       return this.productDisPrice;
   }

   public void setProductDisPrice(Integer productDisPrice) {
       this.productDisPrice = productDisPrice;
   }

   @Column(name = "productSpec")
   @NotEmpty(message = "商品規格: 請勿空白")
   @Size(max = 10, message = "商品規格: 長度不能超過{max}")
   public String getProductSpec() {
       return this.productSpec;
   }

   public void setProductSpec(String productSpec) {
       this.productSpec = productSpec;
   }

   @Column(name = "memCouponNo")
   public Integer getMemCouponNo() {
       return this.memCouponNo;
   }

   public void setMemCouponNo(Integer memCouponNo) {
       this.memCouponNo = memCouponNo;
   }
}