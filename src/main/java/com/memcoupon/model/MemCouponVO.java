package com.memcoupon.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.coupon.model.CouponVO;
import com.member.model.MemberVO;

@Entity
@Table(name = "memCoupon")
public class MemCouponVO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memCouponNo")
    private Integer memCouponNo;
    
    @ManyToOne
    @JoinColumn(name = "memNo", referencedColumnName = "memNo", nullable = false)
    private MemberVO member;
    
    @ManyToOne
    @JoinColumn(name = "couponNo", referencedColumnName = "couponNo", nullable = false)
    private CouponVO coupon;
    
    @Column(name = "status")
    private Integer status;  // 添加狀態欄位，0:未使用, 1:已使用
    
    public MemCouponVO() {
    }
    
    // Getters and Setters
    public Integer getMemCouponNo() {
        return this.memCouponNo;
    }
    
    public void setMemCouponNo(Integer memCouponNo) {
        this.memCouponNo = memCouponNo;
    }
    
    public MemberVO getMember() {
        return this.member;
    }
    
    public void setMember(MemberVO member) {
        this.member = member;
    }
    
    public CouponVO getCoupon() {
        return this.coupon;
    }
    
    public void setCoupon(CouponVO coupon) {
        this.coupon = coupon;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    
}