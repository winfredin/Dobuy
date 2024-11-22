package com.used.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.usedpic.model.UsedPicVO;

@Entity
@Table(name = "used") // 對應資料表名稱
public class UsedVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自動生成主鍵
    @Column(name = "usedNo") // 二手商品編號
    private Integer usedNo;

    @Column(name = "classNo", nullable = false) // 商品類別編號
    @NotNull(message="商品類別編號: 請勿空白")
    @Min(value = 1, message = "商品類別編號: 必須為數字")
    @Max(value = 9999999999L, message = "商品類別編號: 長度不得超過 10 位數")
    private Integer classNo;

    @Column(name = "sellerNo", nullable = false) // 賣家會員編號
    @NotNull(message="賣家編號: 請勿空白")
    @Min(value = 1, message = "賣家編號: 必須為數字")
    @Max(value = 9999999999L, message = "賣家編號: 長度不得超過 10 位數")
    private Integer sellerNo;

    @Column(name = "usedName", nullable = false, length = 20) // 商品名稱
    @NotEmpty(message="商品名稱: 請勿空白")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5\\u3105-\\u312D\\u02B0-\\u02FFa-zA-Z0-9_\\-~`!，。、；;@#$%^&*()\\[\\]{};:'\",.<>/?|+=]{1,30}$", message = "商品名稱: 只能是中、英文字母、數字和特殊符號 , 且長度必需在1到20之間")
    private String usedName;

    @Column(name = "usedProDesc", nullable = false, length = 255) // 商品描述
    @NotEmpty(message="商品描述: 請勿空白")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5\\u3105-\\u312D\\u02B0-\\u02FFa-zA-Z0-9_\\-~`!，。、；;@#$%^&*()\\[\\]{};:'\",.<>/?|+=]{1,200}$", message = "商品描述:只能是中、英文字母、數字和特殊符號 , 且 長度必需在1到250到之間")
    private String usedProDesc;

    @Column(name = "usedNewness", nullable = false) // 商品新舊程度
    @NotNull(message="商品新舊程度: 請勿空白")
    private Integer usedNewness;

    @Column(name = "usedPrice", nullable = false) // 商品單價
    @NotNull(message="商品單價: 請勿空白")
    @Min(value = 1, message = "商品單價: 只能是數字且不能小於{value}")
    private Integer usedPrice;

    @Column(name = "usedStocks", nullable = false) // 庫存數量
    @NotNull(message="庫存數量: 請勿空白")
    @Min(value = 0, message = "庫存量: 只能是數字且不能小於{value}")
    private Integer usedStocks;

    @Column(name = "usedLaunchedTime", updatable = false, insertable = false) // 上架時間由資料庫處理
    private Timestamp usedLaunchedTime;

    @Column(name = "soldTime", updatable = false, insertable = false) // 下架時間由資料庫處理
    private Timestamp soldTime;

    @Column(name = "usedState", nullable = false) // 商品狀態
    @NotNull(message = "商品狀態: 請勿空白")
    private Integer usedState;
//==================================================
    //關聯屬性
    @OneToMany(mappedBy = "usedVO", cascade = CascadeType.ALL, fetch = FetchType.EAGER) // 一對多關聯
    @OrderBy("usedPicNo asc")
    private List<UsedPicVO> usedPics = new ArrayList<UsedPicVO>(); // 用於儲存商品照片列表

    // 相關GET/SET

    public List<UsedPicVO> getUsedPics() {
        return this.usedPics;
    }

    public void setUsedPics(List<UsedPicVO> usedPics) {
        this.usedPics = usedPics;
    }

//==================================================
    // 預設建構子
    public UsedVO() {
        super();
    }

    // Getter 和 Setter
    public Integer getUsedNo() {
        return usedNo;
    }

    public void setUsedNo(Integer usedNo) {
        this.usedNo = usedNo;
    }

    public Integer getClassNo() {
        return classNo;
    }

    public void setClassNo(Integer classNo) {
        this.classNo = classNo;
    }

    public Integer getSellerNo() {
        return sellerNo;
    }

    public void setSellerNo(Integer sellerNo) {
        this.sellerNo = sellerNo;
    }

    public String getUsedName() {
        return usedName;
    }

    public void setUsedName(String usedName) {
        this.usedName = usedName;
    }

    public String getUsedProDesc() {
        return usedProDesc;
    }

    public void setUsedProDesc(String usedProDesc) {
        this.usedProDesc = usedProDesc;
    }

    public Integer getUsedNewness() {
        return usedNewness;
    }

    public void setUsedNewness(Integer usedNewness) {
        this.usedNewness = usedNewness;
    }

    public Integer getUsedPrice() {
        return usedPrice;
    }

    public void setUsedPrice(Integer usedPrice) {
        this.usedPrice = usedPrice;
    }

    public Integer getUsedStocks() {
        return usedStocks;
    }

    public void setUsedStocks(Integer usedStocks) {
        this.usedStocks = usedStocks;
    }

    public Timestamp getUsedLaunchedTime() {
        return usedLaunchedTime;
    }

    public Timestamp getSoldTime() {
        return soldTime;
    }

    public Integer getUsedState() {
        return usedState;
    }

    public void setUsedState(Integer usedState) {
        this.usedState = usedState;
    }
}
