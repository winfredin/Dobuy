package com.counterHome.couponTest.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "NewCoupons")
public class NewCouponsVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增
    @Column(name = "couponNo")
    private Integer couponNo; // 优惠券编号

    @Column(name = "counterNo", nullable = false)
    private Integer counterNo; // 柜位编号

    @Column(name = "couponTitle", nullable = false, length = 255)
    private String couponTitle; // 优惠券标题

    @Column(name = "couponContext", nullable = false, length = 500)
    private String couponContext; // 优惠券内容描述

    @Column(name = "couponStart", nullable = false)
    private LocalDateTime couponStart; // 优惠券开始时间

    @Column(name = "couponEnd", nullable = false)
    private LocalDateTime couponEnd; // 优惠券结束时间

    @Column(name = "couponStatus", nullable = false)
    private Byte couponStatus; // 优惠券状态 (0: 无效, 1: 可用, 2: 过期)

    @Column(name = "usageLimit", nullable = false)
    private Integer usageLimit; // 使用限制次数

    @Column(name = "checkStatus", nullable = false)
    private Byte checkStatus; // 检查状态 (0: 未审核, 1: 已审核)

    // Getters and Setters
    public Integer getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(Integer couponNo) {
        this.couponNo = couponNo;
    }

    public Integer getCounterNo() {
        return counterNo;
    }

    public void setCounterNo(Integer counterNo) {
        this.counterNo = counterNo;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }

    public String getCouponContext() {
        return couponContext;
    }

    public void setCouponContext(String couponContext) {
        this.couponContext = couponContext;
    }

    public LocalDateTime getCouponStart() {
        return couponStart;
    }

    public void setCouponStart(LocalDateTime couponStart) {
        this.couponStart = couponStart;
    }

    public LocalDateTime getCouponEnd() {
        return couponEnd;
    }

    public void setCouponEnd(LocalDateTime couponEnd) {
        this.couponEnd = couponEnd;
    }

    public Byte getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(Byte couponStatus) {
        this.couponStatus = couponStatus;
    }

    public Integer getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }

    public Byte getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Byte checkStatus) {
        this.checkStatus = checkStatus;
    }

}
