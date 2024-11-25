package com.coupon.model;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "coupon") // 对应数据库中的表名
public class CouponVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer couponNo;       // 优惠券编号 (主键)
    private Integer counterNo;      // 櫃位编号 (外键)
    private String couponTitle;     // 优惠券名称
    private String couponContext;   // 优惠券内容
    private Date couponStart;       // 优惠券起始日
    private Date couponEnd;         // 优惠券到期日
    private Integer couponStatus;   // 优惠券状态 (0:未开, 1:有效, 2:过期)
    private Integer usageLimit;     // 领取次数
    private Integer checkStatus;    // 审核状态 (0:未审核, 1:已审核)

    public CouponVO() {
    }

    @Id
    @Column(name = "couponNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主键
    public Integer getCouponNo() {
        return this.couponNo;
    }

    public void setCouponNo(Integer couponNo) {
        this.couponNo = couponNo;
    }

    @Column(name = "counterNo")
    @NotNull(message = "櫃位編號: 請勿空白")
    public Integer getCounterNo() {
        return this.counterNo;
    }

    public void setCounterNo(Integer counterNo) {
        this.counterNo = counterNo;
    }

    @Column(name = "couponTitle")
    @NotEmpty(message = "優惠券名稱: 請勿空白")
    @Size(max = 255, message = "優惠券名稱: 長度不能超過{max}")
    public String getCouponTitle() {
        return this.couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }

    @Column(name = "couponContext")
    @NotEmpty(message = "優惠券內容: 請勿空白")
    @Size(max = 255, message = "優惠券內容: 長度不能超過{max}")
    public String getCouponContext() {
        return this.couponContext;
    }

    public void setCouponContext(String couponContext) {
        this.couponContext = couponContext;
    }

    @Column(name = "couponStart")
    @NotNull(message = "優惠券起始日: 請勿空白")
    @PastOrPresent(message = "優惠券起始日: 必須是過去或現在的時間")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getCouponStart() {
        return this.couponStart;
    }

    public void setCouponStart(Date couponStart) {
        this.couponStart = couponStart;
    }

    @Column(name = "couponEnd")
    @NotNull(message = "優惠券到期日: 請勿空白")
    @Future(message = "優惠券到期日: 必須是未來的時間")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getCouponEnd() {
        return this.couponEnd;
    }

    public void setCouponEnd(Date couponEnd) {
        this.couponEnd = couponEnd;
    }

    @Column(name = "couponStatus")
    @NotNull(message = "優惠券狀態: 請勿空白")
    @Min(value = 0, message = "優惠券狀態: 最小值為{value}")
    @Max(value = 2, message = "優惠券狀態: 最大值為{value}")
    public Integer getCouponStatus() {
        return this.couponStatus;
    }

    public void setCouponStatus(Integer couponStatus) {
        this.couponStatus = couponStatus;
    }

    @Column(name = "usageLimit")
    @NotNull(message = "領取次數: 請勿空白")
    @Min(value = 1, message = "領取次數: 必須大於等於{value}")
    public Integer getUsageLimit() {
        return this.usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }

    @Column(name = "checkStatus")
    @NotNull(message = "審核狀態: 請勿空白")
    @Min(value = 0, message = "審核狀態: 最小值為{value}")
    @Max(value = 1, message = "審核狀態: 最大值為{value}")
    public Integer getCheckStatus() {
        return this.checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }
}
