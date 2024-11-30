package com.ShoppingCartList.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

/**
 * ShoppingCartListVO 實體類別，對應到 ShoppingCartList 資料表
 */
@Entity
@Table(name = "ShoppingCartList")
public class ShoppingCartListVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer shoppingcartListNo;
    private Integer memNo;
    private Integer goodsNo;
    private String goodsName;
    private Integer goodsPrice;
    private Integer goodsNum;
    private Integer orderTotalprice;

    // 無參數建構子
    public ShoppingCartListVO() {}

    // 自增主鍵 shoppingcartListNo
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 設定為自增主鍵
    @Column(name = "shoppingcartListNo", nullable = false, unique = true)
    public Integer getShoppingcartListNo() {
        return this.shoppingcartListNo;
    }

    public void setShoppingcartListNo(Integer shoppingcartListNo) {
        this.shoppingcartListNo = shoppingcartListNo;
    }

    // 會員編號 (memNo)
    @Column(name = "memNo")
//    @NotNull(message = "會員編號: 請勿空白")
    public Integer getMemNo() {
        return this.memNo;
    }

    public void setMemNo(Integer memNo) {
        this.memNo = memNo;
    }

    // 商品編號 (goodsNo)
    @Column(name = "goodsNo", nullable = false)
    @NotNull(message = "商品編號: 請勿空白")
    public Integer getGoodsNo() {
        return this.goodsNo;
    }

    public void setGoodsNo(Integer goodsNo) {
        this.goodsNo = goodsNo;
    }

    // 商品名稱 (goodsName)
    @Column(name = "goodsName", nullable = false, length = 100)
    @NotNull(message = "商品名稱: 請勿空白")
    @Size(max = 100, message = "商品名稱: 不能超過 100 字元")
    public String getGoodsName() {
        return this.goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    // 商品單價 (goodsPrice)
    @Column(name = "goodsPrice", nullable = false)
    @NotNull(message = "商品單價: 請勿空白")
    @Range(min = 1, message = "商品單價: 必須大於 0")
    public Integer getGoodsPrice() {
        return this.goodsPrice;
    }

    public void setGoodsPrice(Integer goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    // 商品數量 (goodsNum)
    @Column(name = "goodsNum", nullable = false)
    @NotNull(message = "商品數量: 請勿空白")
    @PositiveOrZero(message = "商品數量: 必須為正整數")
    public Integer getGoodsNum() {
        return this.goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    // 訂單總金額 (orderTotalprice)
    @Column(name = "orderTotalprice", nullable = false)
//    @NotNull(message = "訂單總金額: 請勿空白")
    @Range(min = 1, message = "訂單總金額: 必須大於 0")
    public Integer getOrderTotalprice() {
        return this.orderTotalprice;
    }

    public void setOrderTotalprice(Integer orderTotalprice) {
        this.orderTotalprice = orderTotalprice;
    }
}
