package com.counterorder.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "counterorder")
public class CounterOrderVO implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private Integer counterOrderNo;
	private Integer counterNo;
	private Integer memNo;
	private Integer orderTotalBefore;
	private Integer orderTotalAfter;
	private Integer orderStatus;
	private String receiverName;
	private String receiverAdr;
	private String receiverPhone;
	private Integer sellerSatisfaction;
	private String sellerCommentContent;
	private Date sellerCommentDate;
	private Date ordertime;
	private Integer disno;
	
	
	public CounterOrderVO(){
		
	}

	@Id
	@Column(name = "counterOrderNo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getCounterOrderNo() {
		return counterOrderNo;
	}


	public void setCounterOrderNo(Integer counterOrderNo) {
		this.counterOrderNo = counterOrderNo;
	}

	@Column(name = "counterNo")
	public Integer getCounterNo() {
		return counterNo;
	}


	public void setCounterNo(Integer counterNo) {
		this.counterNo = counterNo;
	}

	@Column(name = "memNo")
	public Integer getMemNo() {
		return memNo;
	}


	public void setMemNo(Integer memNo) {
		this.memNo = memNo;
	}

	@Column(name = "orderTotalPriceBefore")
	public Integer getOrderTotalBefore() {
		return orderTotalBefore;
	}


	public void setOrderTotalBefore(Integer orderTotalBefore) {
		this.orderTotalBefore = orderTotalBefore;
	}

	@Column(name = "orderTotalPriceAfter")
	public Integer getOrderTotalAfter() {
		return orderTotalAfter;
	}


	public void setOrderTotalAfter(Integer orderTotalAfter) {
		this.orderTotalAfter = orderTotalAfter;
	}

	@Column(name = "orderStatus")
	public Integer getOrderStatus() {
		return orderStatus;
	}


	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Column(name = "receiverName")
	public String getReceiverName() {
		return receiverName;
	}


	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	@Column(name = "receiverAdr")
	public String getReceiverAdr() {
		return receiverAdr;
	}


	public void setReceiverAdr(String receiverAdr) {
		this.receiverAdr = receiverAdr;
	}

	@Column(name = "receiverPhone")
	public String getReceiverPhone() {
		return receiverPhone;
	}


	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	@Column(name = "sellerSatisfaction")
	public Integer getSellerSatisfaction() {
		return sellerSatisfaction;
	}


	public void setSellerSatisfaction(Integer sellerSatisfaction) {
		this.sellerSatisfaction = sellerSatisfaction;
	}

	@Column(name = "sellerCommentContent")
	public String getSellerCommentContent() {
		return sellerCommentContent;
	}


	public void setSellerCommentContent(String sellerCommentContent) {
		this.sellerCommentContent = sellerCommentContent;
	}
	
	@Column(name = "sellerCommentDate")
	public Date getSellerCommentDate() {
		return sellerCommentDate;
	}


	public void setSellerCommentDate(Date sellerCommentDate) {
		this.sellerCommentDate= sellerCommentDate;
	}

	@Column(name = "orderTime")
	public Date getOrdertime() {
		return ordertime;
	}


	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}

	@Column(name="disNo")
	public Integer getDisno() {
		return disno;
	}


	public void setDisno(Integer disno) {
		this.disno = disno;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}