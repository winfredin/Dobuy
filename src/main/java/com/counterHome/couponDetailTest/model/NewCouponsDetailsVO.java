package com.counterHome.couponDetailTest.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "NewCouponDetails")
public class NewCouponsDetailsVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增
    @Column(name = "couponDetailNo")
    private Integer couponDetailNo; // 唯一主键

    @Column(name = "couponNo", nullable = false)
    private Integer couponNo; // 优惠券编号

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt; // 创建时间

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt; // 更新时间

    @Column(name = "thresholdAmount", nullable = false, precision = 10, scale = 2)
    private Double thresholdAmount; // 满额条件 (如满1000元)

    @Column(name = "discountAmount", nullable = false, precision = 10, scale = 2)
    private Double discountAmount; // 折扣金额 (如减100元)

    @Column(name = "discountRate", nullable = false, precision = 5, scale = 2)
    private Double discountRate; // 折扣率 (百分比形式，如 0.10 表示 10%)

    // Getters and Setters
    public Integer getCouponDetailNo() {
        return couponDetailNo;
    }

    public void setCouponDetailNo(Integer couponDetailNo) {
        this.couponDetailNo = couponDetailNo;
    }

    public Integer getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(Integer couponNo) {
        this.couponNo = couponNo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Double getThresholdAmount() {
        return thresholdAmount;
    }

    public void setThresholdAmount(Double thresholdAmount) {
        this.thresholdAmount = thresholdAmount;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

}
