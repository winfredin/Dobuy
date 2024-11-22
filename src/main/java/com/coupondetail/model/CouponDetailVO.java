package com.coupondetail.model;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "couponDetail") // 对应数据库中的表名
public class CouponDetailVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer couponDetailNo; // 优惠券明细编号 (主键)
    private Integer couponNo;       // 优惠券编号 (外键)
    private Integer goodsNo;        // 商品编号 (外键)
    private Date createdAt;         // 创建时间
    private Date updatedAt;         // 更新时间
    private String counterContext;  // 优惠券条件
    private Double disRate;         // 折扣率
    private byte[] upFiles;         // 上传文件

    public CouponDetailVO() {
    }

    @Id
    @Column(name = "couponDetailNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主键
    public Integer getCouponDetailNo() {
        return this.couponDetailNo;
    }

    public void setCouponDetailNo(Integer couponDetailNo) {
        this.couponDetailNo = couponDetailNo;
    }

    @Column(name = "couponNo")
    @NotNull(message = "優惠券編號: 請勿空白")
    public Integer getCouponNo() {
        return this.couponNo;
    }

    public void setCouponNo(Integer couponNo) {
        this.couponNo = couponNo;
    }

    @Column(name = "goodsNo")
    @NotNull(message = "商品編號: 請勿空白")
    public Integer getGoodsNo() {
        return this.goodsNo;
    }

    public void setGoodsNo(Integer goodsNo) {
        this.goodsNo = goodsNo;
    }

    @Column(name = "createdAt")
    @NotNull(message = "建立時間: 請勿空白")
    @PastOrPresent(message = "建立時間: 必須是過去或現在的時間")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Column(name = "updatedAt")
    @NotNull(message = "更新時間: 請勿空白")
    @PastOrPresent(message = "更新時間: 必須是過去或現在的時間")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Column(name = "counterContext")
    @NotEmpty(message = "優惠券條件: 請勿空白")
    @Size(max = 255, message = "優惠券條件: 長度不能超過{max}")
    public String getCounterContext() {
        return this.counterContext;
    }

    public void setCounterContext(String counterContext) {
        this.counterContext = counterContext;
    }

    @Column(name = "disRate")
    @NotNull(message = "折扣率: 請勿空白")
    @DecimalMin(value = "0.01", message = "折扣率: 必須大於等於{value}")
    @DecimalMax(value = "1.00", message = "折扣率: 必須小於等於{value}")
    public Double getDisRate() {
        return this.disRate;
    }

    public void setDisRate(Double disRate) {
        this.disRate = disRate;
    }

//    @Column(name = "upFiles")
//    public byte[] getUpFiles() {
//        return this.upFiles;
//    }
//
//    public void setUpFiles(byte[] upFiles) {
//        this.upFiles = upFiles;
//    }
}
