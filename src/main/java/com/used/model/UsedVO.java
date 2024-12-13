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
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Integer classNo;

    @Column(name = "sellerNo", nullable = false) // 賣家會員編號
    @NotNull(message="賣家編號: 請勿空白")
    @Min(value = 1, message = "賣家編號: 必須為數字")
    private Integer sellerNo;

    @Column(name = "usedName", nullable = false, length = 30) // 商品名稱
    @NotEmpty(message="商品名稱: 請勿空白")
    @Pattern(regexp = "^[\\u4e00-\\u9fffA-Za-z0-9\\s\\p{P}\\p{S}\\u3100-\\u312F\\u31A0-\\u31BF\\u3040-\\u309F\\u30A0-\\u30FF]{1,60}$", message = "商品名稱: 只能是中、英、日文字母、數字和特殊符號 , 且最大長度為60個字")
    @Size( max = 30, message = "名稱長度最大為30字")
    private String usedName;

    @Column(name = "usedProDesc", nullable = false, length = 200) // 商品描述
    @NotEmpty(message="商品描述: 請勿空白")
    @Pattern(regexp = "^[\\u4e00-\\u9fffA-Za-z0-9\\s\\p{P}\\p{S}\\u3100-\\u312F\\u31A0-\\u31BF\\u3040-\\u309F\\u30A0-\\u30FF]{1,200}$"
    		, message = "商品描述:只能是中、英、日文字母、數字和特殊符號 , 且最大長度為200個字")
    @Size( max = 200, message = "商品描述最大長度為200個字")
    private String usedProDesc;

    @Column(name = "usedNewness", nullable = false) // 商品新舊程度
    private Integer usedNewness;

    @Column(name = "usedPrice", nullable = false) // 商品單價
    @NotNull(message="商品單價: 請勿空白")
    @Digits(integer = 6, fraction = 0, message = "商品單價:數字最多6位")
    private Integer usedPrice;

    @Column(name = "usedStocks", nullable = false) // 庫存數量
    @NotNull(message="庫存數量: 請勿空白")
    @Min(value = 0, message = "庫存量: 只能是數字且不能小於{value}")
    @Digits(integer = 5, fraction = 0, message = "庫存量:數字最多5位")
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
    @JsonIgnore
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
    public UsedVO(Integer usedNo,String usedName,List<UsedPicVO> usedPics ) {
        this.usedNo=usedNo;
        this.usedName=usedName;
        this.usedPics=usedPics;
        
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
