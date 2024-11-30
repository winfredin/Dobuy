package com.countercarousel.model;

import java.sql.Timestamp;
import java.util.Base64;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "countercarousel")
public class CountercarouselVO {
	
	@Id
	@Column(name = "counterCarouselNo")
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自動生成主鍵
	private Integer id;
	
	@Column(name = "counterNo")
	private Integer counterNo;
	
	@Column(name = "carouselTime", insertable = false, updatable = false)
	private Timestamp carouselTime;

	
	@Transient // 標記為非持久化字段
	private MultipartFile upFile;
	
	@Column(name = "carouselPic") 
	private byte[] carouselPic;
	
	@Transient // 非持久化字段
	private String base64Image;
	
	@Column(name = "goodsNo")
	private Integer goodsNo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCounterNo() {
		return counterNo;
	}

	public void setCounterNo(Integer counterNo) {
		this.counterNo = counterNo;
	}

	public MultipartFile getUpFile() {
		return upFile;
	}

	public void setUpFile(MultipartFile upFile) {
		this.upFile = upFile;
	}

	public byte[] getCarouselPic() {
		return carouselPic;
	}

	public void setCarouselPic(byte[] carouselPic) {
		this.carouselPic = carouselPic;
	}

	public String getBase64Image() {
		return base64Image;
	}

	public void setBase64Image(String base64Image) {
		this.base64Image = base64Image;
	}
	
	public void convertToBase64() {
        if (carouselPic != null) {
        	this.base64Image = Base64.getEncoder().encodeToString(this.carouselPic);
        }
    }

	public Integer getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(Integer goodsNo) {
		this.goodsNo = goodsNo;
	}
	
	// Getter 和 Setter
	public Timestamp getCarouselTime() {
	    return carouselTime;
	}

	public void setCarouselTime(Timestamp carouselTime) {
	    this.carouselTime = carouselTime;
	}
	
}
