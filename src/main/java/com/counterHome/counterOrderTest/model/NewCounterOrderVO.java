package com.counterHome.counterOrderTest.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "newcounterorder")
public class NewCounterOrderVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cOrderNo")
    private Integer cOrderNo; // 櫃位訂單編號 (主鍵)

    @Column(name = "counterNo", nullable = false)
    private Integer counterNo; // 櫃位編號 (外鍵)

    @Column(name = "memNo", nullable = false)
    private Integer memNo; // 會員編號 (外鍵)

    @Column(name = "orderTotalPriceBefore", nullable = false)
    private Integer orderTotalPriceBefore; // 訂單總金額(折前)

    @Column(name = "orderTotalPriceAfter", nullable = false)
    private Integer orderTotalPriceAfter; // 訂單總金額(折後)

    @Column(name = "receiverName", length = 10, nullable = false)
    private String receiverName; // 收件人姓名

    @Column(name = "receiverAdr", length = 100, nullable = false)
    private String receiverAdr; // 收件人地址

    @Column(name = "receiverPhone", length = 10, nullable = false)
    private String receiverPhone; // 收件人電話

    @Column(name = "couponNo")
    private Integer couponNo; // 會員優惠券編號

    @Column(name = "orderTime", insertable = false, updatable = false)
    private Timestamp orderTime; // 訂單成立時間 (默認當前時間)

    @Column(name = "orderStatus", nullable = false)
    private Integer orderStatus; // 訂單狀態 (0:未出貨, 1:已出貨, 2:完成訂單, 3:退貨, 4:作廢)

    // Getters 和 Setters
    public Integer getcOrderNo() {
        return cOrderNo;
    }

    public void setcOrderNo(Integer cOrderNo) {
        this.cOrderNo = cOrderNo;
    }

    public Integer getCounterNo() {
        return counterNo;
    }

    public void setCounterNo(Integer counterNo) {
        this.counterNo = counterNo;
    }

    public Integer getMemNo() {
        return memNo;
    }

    public void setMemNo(Integer memNo) {
        this.memNo = memNo;
    }

    public Integer getOrderTotalPriceBefore() {
        return orderTotalPriceBefore;
    }

    public void setOrderTotalPriceBefore(Integer orderTotalPriceBefore) {
        this.orderTotalPriceBefore = orderTotalPriceBefore;
    }

    public Integer getOrderTotalPriceAfter() {
        return orderTotalPriceAfter;
    }

    public void setOrderTotalPriceAfter(Integer orderTotalPriceAfter) {
        this.orderTotalPriceAfter = orderTotalPriceAfter;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAdr() {
        return receiverAdr;
    }

    public void setReceiverAdr(String receiverAdr) {
        this.receiverAdr = receiverAdr;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public Integer getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(Integer couponNo) {
        this.couponNo = couponNo;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

}
