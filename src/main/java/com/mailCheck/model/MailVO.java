package com.mailCheck.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "emailverification")
public class MailVO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	
	

	private String email; // 郵箱地址
	private String verificationCode; // 驗證碼
	private int isVerified; // 是否已驗證（0: 未驗證, 1: 已驗證）
	@Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp sentTime; // 發送驗證碼的時間

	// 全參構造函數
	public MailVO(String email, String verificationCode, int isVerified) {
		this.email = email;
		this.verificationCode = verificationCode;
		this.isVerified = isVerified;
	}

	public MailVO() {
		// TODO Auto-generated constructor stub
	}

	// Getter 和 Setter 方法
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public int getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(int isVerified) {
		this.isVerified = isVerified;
	}

	public Timestamp getSentTime() {
		return sentTime;
	}

	public void setSentTime(Timestamp sentTime) {
		this.sentTime = sentTime;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
