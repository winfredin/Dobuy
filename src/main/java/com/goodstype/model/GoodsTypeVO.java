package com.goodstype.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "GoodsType")
public class GoodsTypeVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer goodstNo; // 商品類別編號
    private String goodstName; // 商品類別名稱

    public GoodsTypeVO() {
    }

    @Id
    @Column(name = "goodstNo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getGoodstNo() {
        return this.goodstNo;
    }

    public void setGoodstNo(Integer goodstNo) {
        this.goodstNo = goodstNo;
    }

    @Column(name = "goodstName")
    @NotNull(message = "商品類別名稱: 請勿空白")
    @Size(min = 1, max = 255, message = "商品類別名稱: 長度必需在{min}到{max}之間")
    public String getGoodstName() {
        return this.goodstName;
    }

    public void setGoodstName(String goodstName) {
        this.goodstName = goodstName;
    }
}
