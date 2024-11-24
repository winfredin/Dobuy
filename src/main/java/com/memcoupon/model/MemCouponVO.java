package com.memcoupon.model;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "memCoupon") // 對應資料庫中的表名
public class MemCouponVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer memCouponNo; // 會員優惠券編號 (主鍵)
    private Integer memNo;       // 會員編號 (外鍵)
    private Integer couponNo;    // 優惠券編號 (外鍵)

    public MemCouponVO() {
    }

    @Id
    @Column(name = "memCouponNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主鍵
    public Integer getMemCouponNo() {
        return this.memCouponNo;
    }

    public void setMemCouponNo(Integer memCouponNo) {
        this.memCouponNo = memCouponNo;
    }

    @Column(name = "memNo")
    @NotNull(message = "會員編號: 請勿空白")
    public Integer getMemNo() {
        return this.memNo;
    }

    public void setMemNo(Integer memNo) {
        this.memNo = memNo;
    }

    @Column(name = "couponNo")
    @NotNull(message = "優惠券編號: 請勿空白")
    public Integer getCouponNo() {
        return this.couponNo;
    }

    public void setCouponNo(Integer couponNo) {
        this.couponNo = couponNo;
    }
}
