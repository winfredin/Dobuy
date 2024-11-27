package com.goods.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.counter.model.CounterVO;
import com.goodstype.model.GoodsTypeVO;

@Entity
@Table(name = "Goods")
public class GoodsVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer goodsNo; // 商品編號
    private GoodsTypeVO goodsTypeVO; // 商品類別 (關聯至商品類別表)
    private CounterVO counterVO; // 櫃位編號 (關聯至櫃位表)
    private String goodsName; // 商品名稱
    private String goodsDescription; // 商品敘述
    private Integer goodsPrice; // 商品單價
    private Integer goodsAmount; // 商品庫存
    private byte[] gpPhotos1; // 商品主圖(必填)
    private byte[] gpPhotos2; // 商品副圖1(選填)
    private byte[] gpPhotos3; // 商品副圖2(選填)
    private byte[] gpPhotos4; // 商品副圖3(選填)
    private byte[] gpPhotos5; // 商品副圖4(選填)
    private byte[] gpPhotos6; // 商品副圖5(選填)
    private byte[] gpPhotos7; // 商品副圖6(選填)
    private byte[] gpPhotos8; // 商品副圖7(選填)
    private byte[] gpPhotos9; // 商品副圖8(選填)
    private byte[] gpPhotos10; // 商品副圖9(選填)
    private Byte goodsStatus; // 商品狀態 (0：下架  1：上架)
    private Byte checkStatus; // 審核狀態 (0：審核中 1：通過 2：未通過)
    private Timestamp goodsDate; // 商品上架日期
    private Timestamp goodsEnddate; // 商品下架日期

    public GoodsVO() {
    }

    @Id
    @Column(name = "goodsNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getGoodsNo() {
        return this.goodsNo;
    }

    public void setGoodsNo(Integer goodsNo) {
        this.goodsNo = goodsNo;
    }
    
	@ManyToOne
	@JoinColumn(name = "goodstNo")   // 指定用來join table的column
    public GoodsTypeVO getGoodsTypeVO() {
        return this.goodsTypeVO;
    }

    public void setGoodsTypeVO(GoodsTypeVO goodsTypeVO) {
        this.goodsTypeVO = goodsTypeVO;
    }

	@ManyToOne
	@JoinColumn(name = "counterNo")   // 指定用來join table的column
    public CounterVO getCounterVO() {
        return this.counterVO;
    }

    public void setCounterVO(CounterVO counterVO) {
        this.counterVO = counterVO;
    }

    @Column(name = "goodsName")
    @NotNull(message = "商品名稱: 請勿空白")
    @Size(min = 1, max = 500, message = "商品名稱: 長度必需在{min}到{max}之間")
    public String getGoodsName() {
        return this.goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    @Column(name = "goodsDescription")
    @NotNull(message = "商品敘述: 請勿空白")
    @Size(min = 1, max = 500, message = "商品敘述: 長度必需在{min}到{max}之間")
    public String getGoodsDescription() {
        return this.goodsDescription;
    }

    public void setGoodsDescription(String goodsDescription) {
        this.goodsDescription = goodsDescription;
    }

    @Column(name = "goodsPrice")
    @NotNull(message = "商品單價: 請勿空白")
    @DecimalMin(value = "1", message = "商品單價: 必須大於或等於{value}")
    public Integer getGoodsPrice() {
        return this.goodsPrice;
    }

    public void setGoodsPrice(Integer goodsPrice) {
        this.goodsPrice = goodsPrice;
    }
    
    @Column(name = "goodsAmount")
    @NotNull(message = "商品庫存: 請勿空白")
    public Integer getGoodsAmount() {
        return this.goodsAmount;
    }

    public void setGoodsAmount(Integer goodsAmount) {
        this.goodsAmount = goodsAmount;
    }
    @Lob
    @Column(name = "gpPhotos1")
//    @NotNull(message = "商品主圖(必填): 請勿空白")
    public byte[] getGpPhotos1() {
        return this.gpPhotos1;
    }

    public void setGpPhotos1(byte[] gpPhotos1) {
        this.gpPhotos1 = gpPhotos1;
    }
    @Lob
    @Column(name = "gpPhotos2")
    public byte[] getGpPhotos2() {
        return this.gpPhotos2;
    }

    public void setGpPhotos2(byte[] gpPhotos2) {
        this.gpPhotos2 = gpPhotos2;
    }
    @Lob
    @Column(name = "gpPhotos3")
    public byte[] getGpPhotos3() {
        return this.gpPhotos3;
    }

    public void setGpPhotos3(byte[] gpPhotos3) {
        this.gpPhotos3 = gpPhotos3;
    }
    @Lob
    @Column(name = "gpPhotos4")
    public byte[] getGpPhotos4() {
        return this.gpPhotos4;
    }

    public void setGpPhotos4(byte[] gpPhotos4) {
        this.gpPhotos4 = gpPhotos4;
    }
    @Lob
    @Column(name = "gpPhotos5")
    public byte[] getGpPhotos5() {
        return this.gpPhotos5;
    }

    public void setGpPhotos5(byte[] gpPhotos5) {
        this.gpPhotos5 = gpPhotos5;
    }
    @Lob
    @Column(name = "gpPhotos6")
    public byte[] getGpPhotos6() {
        return this.gpPhotos6;
    }

    public void setGpPhotos6(byte[] gpPhotos6) {
        this.gpPhotos6 = gpPhotos6;
    }
    @Lob
    @Column(name = "gpPhotos7")
    public byte[] getGpPhotos7() {
        return this.gpPhotos7;
    }

    public void setGpPhotos7(byte[] gpPhotos7) {
        this.gpPhotos7 = gpPhotos7;
    }
    @Lob
    @Column(name = "gpPhotos8")
    public byte[] getGpPhotos8() {
        return this.gpPhotos8;
    }

    public void setGpPhotos8(byte[] gpPhotos8) {
        this.gpPhotos8 = gpPhotos8;
    }
    @Lob
    @Column(name = "gpPhotos9")
    public byte[] getGpPhotos9() {
        return this.gpPhotos9;
    }

    public void setGpPhotos9(byte[] gpPhotos9) {
        this.gpPhotos9 = gpPhotos9;
    }
    @Lob
    @Column(name = "gpPhotos10")
    public byte[] getGpPhotos10() {
        return this.gpPhotos10;
    }

    public void setGpPhotos10(byte[] gpPhotos10) {
        this.gpPhotos10 = gpPhotos10;
    }

    @Column(name = "goodsStatus")
    public Byte getGoodsStatus() {
    	return this.goodsStatus;
    }
    
    public void setGoodsStatus(Byte goodsStatus) {
    	this.goodsStatus = goodsStatus;
    }

    @Column(name = "checkStatus")
    public Byte getCheckStatus() {
    	return this.checkStatus;
    }
    
    public void setCheckStatus(Byte checkStatus) {
    	this.checkStatus = checkStatus;
    }
    
    @Column(name = "goodsDate")
//    @NotNull(message = "商品上架日期: 請勿空白")
//    @PastOrPresent(message="日期必須是在今日(含)之前")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getGoodsDate() {
        return this.goodsDate;
    }

    public void setGoodsDate(Timestamp goodsDate) {
        this.goodsDate = goodsDate;
    }

    @Column(name = "goodsEnddate")
//    @NotNull(message = "商品下架日期: 請勿空白")
//    @FutureOrPresent(message="日期必須是在今日(不含)之後")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getGoodsEnddate() {
        return this.goodsEnddate;
    }

    public void setGoodsEnddate(Timestamp goodsEnddate) {
        this.goodsEnddate = goodsEnddate;
    }
    
}