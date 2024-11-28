package com.coupondetail.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.coupon.model.CouponVO;

@Entity
@Table(name = "coupondetail")
public class CouponDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主键
    @Column(name = "couponDetailNo")
    private Integer couponDetailNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "couponNo", nullable = false)  // 確保非空
    private CouponVO coupon; // 优惠券编号 (外键)

    @Column(name = "goodsNo", nullable = false)
    @NotNull(message = "商品編號: 請勿空白")
    private Integer goodsNo;

    @Column(name = "createdAt", nullable = false)
    @NotNull(message = "建立時間: 請勿空白")
    @PastOrPresent(message = "建立時間: 必須是過去或現在的時間")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @Column(name = "updatedAt", nullable = false)
    @NotNull(message = "更新時間: 請勿空白")
    @PastOrPresent(message = "更新時間: 必須是過去或現在的時間")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;

    @Column(name = "counterContext", nullable = false, length = 255)
    @NotEmpty(message = "優惠券條件: 請勿空白")
    @Size(max = 255, message = "優惠券條件: 長度不能超過{max}")
    private String counterContext;

    @Column(name = "disRate", nullable = false)
    @NotNull(message = "折扣率: 請勿空白")
    @DecimalMin(value = "0.01", message = "折扣率: 必須大於等於{value}")
    @DecimalMax(value = "1.00", message = "折扣率: 必須小於等於{value}")
    private Double disRate;

    // Constructors
    public CouponDetailVO() {
    }

    // 自动设置时间戳
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }

    
    
    // Getters and Setters
    public Integer getCouponDetailNo() {
        return couponDetailNo;
    }

    public void setCouponDetailNo(Integer couponDetailNo) {
        this.couponDetailNo = couponDetailNo;
    }

    public CouponVO getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponVO coupon) {
        this.coupon = coupon;
    }

    public Integer getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(Integer goodsNo) {
        this.goodsNo = goodsNo;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCounterContext() {
        return counterContext;
    }

    public void setCounterContext(String counterContext) {
        this.counterContext = counterContext;
    }

    public Double getDisRate() {
        return disRate;
    }

    public void setDisRate(Double disRate) {
        this.disRate = disRate;
    }
}
