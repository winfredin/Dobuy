package com.customerservice.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "usedComplaint")
public class CustomerServiceVO {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usedComplaintNo")
    private Integer usedComplaintNo;
        
    @Column(name = "usedOrderNo", nullable = false)
    private Integer usedOrderNo;
    
    @Column(name = "memNo")
    private Integer memNo;

    @NotNull
    @NotEmpty(message = "客訴內容請勿空白。")
    @Size(max = 500)
    @Column(name = "usedComplaintReason")
    private String usedComplaintReason;
    
    @Column(name = "usedComplaintPhotos")
    private byte[] usedComplaintPhotos;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "usedComplaintDate") 
    private Timestamp usedComplaintDate;
    
    @Column(name = "usedComplaintStatus")
    private Byte usedComplaintStatus;
    
    @Column(name = "usedComplaintMSG")
    private String usedComplaintMSG;
    
    // Getters and Setters
    public Integer getUsedComplaintNo() {
        return usedComplaintNo;
    }

    public void setUsedComplaintNo(Integer usedComplaintNo) {
        this.usedComplaintNo = usedComplaintNo;
    }

    public Integer getUsedOrderNo() {
        return usedOrderNo;
    }

    public void setUsedOrderNo(Integer usedOrderNo) {
        this.usedOrderNo = usedOrderNo;
    }

    public Integer getMemNo() {
        return memNo;
    }

    public void setMemNo(Integer memNo) {
        this.memNo = memNo;
    }

    public String getUsedComplaintReason() {
        return usedComplaintReason;
    }

    public void setUsedComplaintReason(String usedComplaintReason) {
        this.usedComplaintReason = usedComplaintReason;
    }

    public byte[] getUsedComplaintPhotos() {
        return usedComplaintPhotos;
    }

    public void setUsedComplaintPhotos(byte[] usedComplaintPhotos) {
        this.usedComplaintPhotos = usedComplaintPhotos;
    }

    public Timestamp getUsedComplaintDate() {
        return usedComplaintDate;
    }

    public void setUsedComplaintDate(Timestamp usedComplaintDate) {
        this.usedComplaintDate = usedComplaintDate;
    }

    public Byte getUsedComplaintStatus() {
        return usedComplaintStatus;
    }

    public void setUsedComplaintStatus(Byte usedComplaintStatus) {
        this.usedComplaintStatus = usedComplaintStatus;
    }

    public String getUsedComplaintMSG() {
        return usedComplaintMSG;
    }

    public void setUsedComplaintMSG(String usedComplaintMSG) {
        this.usedComplaintMSG = usedComplaintMSG;
    }
}
