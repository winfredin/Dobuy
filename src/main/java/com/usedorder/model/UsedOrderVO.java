package com.usedorder.model;

import java.sql.Timestamp;
import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "UsedOrder")
public class UsedOrderVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usedOrderNo")
    private Integer usedOrderNo; // 二手訂單編號

    @NotNull
    @Column(name = "usedNo")
    private Integer usedNo; // 二手商品編號

    @NotNull
    @Column(name = "buyerNo")
    private Integer buyerNo; // 買家編號

    @Column(name = "usedOrderTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Timestamp usedOrderTime; // 訂單成立時間

    @NotNull
    @Min(value = 1, message = "二手商品單價必須大於0")
    @Column(name = "usedPrice")
    private Integer usedPrice; // 二手商品單價

    @NotNull
    @Min(value = 1, message = "二手商品購買數量必須大於0")
    @Column(name = "usedCount")
    private Integer usedCount; // 二手商品購買數量

    @NotNull
    @Column(name = "deliveryStatus")
    private Byte deliveryStatus; // 宅配狀態
    
    // 新增的描述方法
    public String getDeliveryStatusDescription() {
        switch (deliveryStatus) {
            case 0:
                return "未出貨";
            case 1:
                return "已出貨";
            case 2:
                return "待領件";
            case 3:
                return "已領貨";
            case 4:
                return "已取消";
            case 5:
                return "已付款"; // 確保5的狀態也能直接處理
            case 6:
            	return "未付款";
            default:
                // 預設為狀態 5 且返回 "已付款"
                return "已付款";
        }
    }

    @Column(name = "receiverName")
    private String receiverName; // 收件人姓名

    @Column(name = "receiverAdr")
    private String receiverAdr; // 收件人地址

    @Column(name = "receiverPhone")
    private String receiverPhone; // 收件人電話

    @Column(name = "sellerSatisfication")
    private Byte sellerSatisfication; // 滿意度

    @Column(name = "sellerCommentContent")
    private String sellerCommentContent; // 評論內文
    
    

	@Column(name = "sellerCommentDate")
    private Timestamp sellerCommentDate; // 評論日期

    @NotNull
    @Min(value = 1, message = "訂單總價必須大於0")
    @Column(name = "usedTotalPrice")
    private Integer usedTotalPrice; // 訂單總價

    // Getters and Setters
    public Integer getUsedOrderNo() {
        return usedOrderNo;
    }

    public void setUsedOrderNo(Integer usedOrderNo) {
        this.usedOrderNo = usedOrderNo;
    }

    public Integer getUsedNo() {
        return usedNo;
    }

    public void setUsedNo(Integer usedNo) {
        this.usedNo = usedNo;
    }

    public Integer getBuyerNo() {
        return buyerNo;
    }

    public void setBuyerNo(Integer buyerNo) {
        this.buyerNo = buyerNo;
    }

    public Timestamp getUsedOrderTime() {
        return usedOrderTime;
    }

    public void setUsedOrderTime(Timestamp usedOrderTime) {
        this.usedOrderTime = usedOrderTime;
    }

    public Integer getUsedPrice() {
        return usedPrice;
    }

    public void setUsedPrice(Integer usedPrice) {
        this.usedPrice = usedPrice;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public Byte getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(Byte deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAdr() {
        return receiverAdr;
    }

    public void setReceiverAdr(String receiverAdr) {
        this.receiverAdr = receiverAdr;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public Byte getSellerSatisfication() {
        return sellerSatisfication;
    }

    public void setSellerSatisfication(Byte sellerSatisfication) {
        this.sellerSatisfication = sellerSatisfication;
    }

    public String getSellerCommentContent() {
        return sellerCommentContent;
    }

    public void setSellerCommentContent(String sellerCommentContent) {
        this.sellerCommentContent = sellerCommentContent;
    }

    public Timestamp getSellerCommentDate() {
        return sellerCommentDate;
    }

    public void setSellerCommentDate(Timestamp sellerCommentDate) {
        this.sellerCommentDate = sellerCommentDate;
    }

    public Integer getUsedTotalPrice() {
        return usedTotalPrice;
    }

    public void setUsedTotalPrice(Integer usedTotalPrice) {
        this.usedTotalPrice = usedTotalPrice;
    }

	@Override
	public String toString() {
		return "UsedOrderVO [usedOrderNo=" + usedOrderNo + ", usedNo=" + usedNo + ", buyerNo=" + buyerNo
				+ ", usedOrderTime=" + usedOrderTime + ", usedPrice=" + usedPrice + ", usedCount=" + usedCount
				+ ", deliveryStatus=" + deliveryStatus + ", receiverName=" + receiverName + ", receiverAdr="
				+ receiverAdr + ", receiverPhone=" + receiverPhone + ", sellerSatisfication=" + sellerSatisfication
				+ ", sellerCommentContent=" + sellerCommentContent + ", sellerCommentDate=" + sellerCommentDate
				+ ", usedTotalPrice=" + usedTotalPrice + "]";
	}
    
    
}

   


