package com.discount.model;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Discount") // 对应数据库中的表名
public class DiscountVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer disNo;         // 平台优惠编号 (主键)
    private String disTitle;       // 平台优惠名称
    private String disContext;     // 平台优惠内容
    private Double disRate;        // 折扣率
    private Integer disStatus;     // 优惠状态
    private String descLimit;      // 使用条件描述
    private Date createdAt;        // 创建时间
    private Date updatedAt;        // 更新时间
    private Date disStart;         // 优惠起始日
    private Date disEnd;           // 优惠结束日

    public DiscountVO() {
    }

    @Id
    @Column(name = "disNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主键
    public Integer getDisNo() {
        return this.disNo;
    }

    public void setDisNo(Integer disNo) {
        this.disNo = disNo;
    }

    @Column(name = "disTitle")
    @NotEmpty(message = "平台優惠名稱: 請勿空白")
    @Size(max = 255, message = "平台優惠名稱: 長度不能超過{max}")
    public String getDisTitle() {
        return this.disTitle;
    }

    public void setDisTitle(String disTitle) {
        this.disTitle = disTitle;
    }

    @Column(name = "disContext")
    @NotEmpty(message = "平台優惠內容: 請勿空白")
    @Size(max = 255, message = "平台優惠內容: 長度不能超過{max}")
    public String getDisContext() {
        return this.disContext;
    }

    public void setDisContext(String disContext) {
        this.disContext = disContext;
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

    @Column(name = "disStatus")
    @NotNull(message = "優惠狀態: 請勿空白")
    @Min(value = 0, message = "優惠狀態: 必須是0或以上")
    @Max(value = 2, message = "優惠狀態: 必須是2或以下")
    public Integer getDisStatus() {
        return this.disStatus;
    }

    public void setDisStatus(Integer disStatus) {
        this.disStatus = disStatus;
    }

    @Column(name = "descLimit")
    @NotEmpty(message = "使用條件描述: 請勿空白")
    @Size(max = 255, message = "使用條件描述: 長度不能超過{max}")
    public String getDescLimit() {
        return this.descLimit;
    }

    public void setDescLimit(String descLimit) {
        this.descLimit = descLimit;
    }

    @Column(name = "createdAt")
    @NotNull(message = "建立時間: 請勿空白")
    @PastOrPresent(message = "建立時間: 必須是過去或現在的時間")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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

    @Column(name = "disStart")
    @NotNull(message = "優惠起始日: 請勿空白")
    @FutureOrPresent(message = "優惠起始日: 必須是現在或未來的時間")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getDisStart() {
        return this.disStart;
    }

    public void setDisStart(Date disStart) {
        this.disStart = disStart;
    }

    @Column(name = "disEnd")
    @NotNull(message = "優惠結束日: 請勿空白")
    @Future(message = "優惠結束日: 必須是未來的時間")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getDisEnd() {
        return this.disEnd;
    }

    public void setDisEnd(Date disEnd) {
        this.disEnd = disEnd;
    }
}
