package com.coupon.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.coupondetail.model.CouponDetailVO;

@Entity
@Table(name = "coupon")
public class CouponVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主鍵
    @Column(name = "couponNo")
    private Integer couponNo;

    @Column(name = "counterNo", nullable = false)
    @NotNull(message = "櫃位編號: 請勿空白")
    private Integer counterNo;

    @Column(name = "couponTitle", nullable = false, length = 255)
    @NotEmpty(message = "優惠券名稱: 請勿空白")
    @Size(max = 255, message = "優惠券名稱: 長度不能超過{max}")
    private String couponTitle;

    @Column(name = "couponContext", nullable = false, length = 255)
    @NotEmpty(message = "優惠券內容: 請勿空白")
    @Size(max = 255, message = "優惠券內容: 長度不能超過{max}")
    private String couponContext;

    @Column(name = "couponStart", nullable = false)
    @NotNull(message = "優惠券起始日: 請勿空白")
    @PastOrPresent(message = "優惠券起始日: 必須是過去或現在的時間")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date couponStart;

    @Column(name = "couponEnd", nullable = false)
    @NotNull(message = "優惠券到期日: 請勿空白")
    @Future(message = "優惠券到期日: 必須是未來的時間")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date couponEnd;

    @Column(name = "couponStatus", nullable = false)
    @NotNull(message = "優惠券狀態: 請勿空白")
    @Min(value = 0, message = "優惠券狀態: 最小值為{value}")
    @Max(value = 2, message = "優惠券狀態: 最大值為{value}")
    private Integer couponStatus;

    @Column(name = "usageLimit", nullable = false)
    @NotNull(message = "領取次數: 請勿空白")
    @Min(value = 1, message = "領取次數: 必須大於等於{value}")
    private Integer usageLimit;

    @Column(name = "checkStatus", nullable = false)
    @NotNull(message = "審核狀態: 請勿空白")
    @Min(value = 0, message = "審核狀態: 最小值為{value}")
    @Max(value = 1, message = "審核狀態: 最大值為{value}")
    private Integer checkStatus;
//    外鍵設置
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CouponDetailVO> couponDetails = new ArrayList<>();

    // 一個更安全的方法來添加明細
    // 修改 addCouponDetail 方法，確保正確設置關聯
    public void addCouponDetail(CouponDetailVO detail) {
        if (couponDetails == null) {
            couponDetails = new ArrayList<>();
        }
        couponDetails.add(detail);
        detail.setCoupon(this);  // 重要：設置雙向關聯
    }
    
    public void removeCouponDetail(CouponDetailVO detail) {
        couponDetails.remove(detail);
        detail.setCoupon(null);
    }
    
    // Constructor
    public CouponVO() {
    }

    // Getters and Setters
    public Integer getCouponNo() {
        return couponNo;
    }
//  外鍵設置
    public void setCouponNo(Integer couponNo) {
        this.couponNo = couponNo;
    }
//  外鍵設置
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

    public Date getCouponStart() {
        return couponStart;
    }

    public void setCouponStart(Date couponStart) {
        this.couponStart = couponStart;
    }

    public Date getCouponEnd() {
        return couponEnd;
    }

    public void setCouponEnd(Date couponEnd) {
        this.couponEnd = couponEnd;
    }

    public Integer getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(Integer couponStatus) {
        this.couponStatus = couponStatus;
    }

    public Integer getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public List<CouponDetailVO> getCouponDetails() {
        return couponDetails;
    }

    public void setCouponDetails(List<CouponDetailVO> couponDetails) {
        this.couponDetails = couponDetails;
    }
}
