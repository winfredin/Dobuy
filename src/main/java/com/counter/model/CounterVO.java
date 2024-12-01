package com.counter.model;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;

//import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.counter.model.CounterVO.LoginGroup;
import com.counter.model.CounterVO.RegisterGroup;
import com.counter.model.CounterVO.UpdateGroup;
import com.counterType.model.CounterTypeVO;

/////test

@Entity // 將此類標記為 JPA 的實體類別
@Table(name = "COUNTER") // 對應資料表名稱
public class CounterVO implements Serializable {
    private static final long serialVersionUID = 1L;
    public interface UpdateGroup {}
    public interface LoginGroup {}
    public interface RegisterGroup {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COUNTERNO")
    private Integer counterNo; // 主鍵自增編號

    @Column(name = "COUNTERACCOUNT", nullable = false, unique = true)
    @NotEmpty(message = "櫃位帳號: 請勿空白" , groups = {LoginGroup.class, RegisterGroup.class})
    @Size(max = 20, message = "櫃位帳號: 長度不能超過20個字元", groups = {RegisterGroup.class})
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "櫃位帳號: 只能包含英文和數字", groups = {RegisterGroup.class})
    private String counterAccount;

    @Column(name = "COUNTERNAME", nullable = false)
    @NotEmpty(message = "櫃位管理者姓名: 請勿空白", groups = {RegisterGroup.class , UpdateGroup.class})
    @Size(max = 45, message = "櫃位管理者姓名: 長度不能超過45個字元", groups = {RegisterGroup.class, UpdateGroup.class})
    private String counterName;

    @Column(name = "COUNTERPASSWORD", nullable = false)
    @NotEmpty(message = "櫃位密碼: 請勿空白" , groups = {LoginGroup.class, RegisterGroup.class})
    @Size(max = 20, message = "櫃位密碼: 長度不能超過20個字元", groups = {RegisterGroup.class})
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "櫃位密碼: 只能包含英文和數字", groups = {RegisterGroup.class})
    private String counterPassword;

    @Column(name = "COUNTERADDRESS", nullable = false)
    @NotEmpty(message = "櫃位地址: 請勿空白" , groups = {RegisterGroup.class, UpdateGroup.class})
    @Size(max = 255, message = "櫃位地址: 長度不能超過255個字元", groups = {RegisterGroup.class, UpdateGroup.class})
    private String counterAddress;

    @Column(name = "COUNTERPHONE", nullable = false)
    @NotEmpty(message = "櫃位電話: 請勿空白", groups = {RegisterGroup.class, UpdateGroup.class})
    @Pattern(regexp = "^09\\d{8}$", message = "櫃位電話: 格式錯誤，必須是以 09 開頭的 10 位數字", groups = {RegisterGroup.class, UpdateGroup.class})
    private String counterPhone;

    @Column(name = "COUNTERUID", nullable = false)
    @NotEmpty(message = "管理者身分證字號: 請勿空白" , groups = {RegisterGroup.class, UpdateGroup.class})
    @Pattern(regexp = "^[A-Z][1-2]\\d{8}$", message = "身分證字號: 格式錯誤，必須是以英文大寫開頭第一個數字必為1或2總長10位", groups = {RegisterGroup.class, UpdateGroup.class})
    private String counterUid;

    @Column(name = "COUNTEREMAIL", nullable = false)
    @NotEmpty(message = "櫃位電子信箱: 請勿空白" , groups = {RegisterGroup.class, UpdateGroup.class})
    @Email(message = "櫃位電子信箱: 格式不正確", groups = {RegisterGroup.class, UpdateGroup.class})
    @Size(max = 100, message = "櫃位電子信箱: 長度不能超過100個字元", groups = {RegisterGroup.class, UpdateGroup.class})
    private String counterEmail;

    @Column(name = "COUNTERUBN", nullable = false, unique = true)
    @NotEmpty(message = "櫃位統一編號: 請勿空白" , groups = {RegisterGroup.class})
    @Pattern(regexp = "^\\d{8}$", message = "櫃位統一編號: 必須為8位數字", groups = {RegisterGroup.class})
    private String counterUbn;

    @Column(name = "COUNTERCNAME", nullable = false)
    @NotEmpty(message = "櫃位名稱: 請勿空白" , groups = {RegisterGroup.class, UpdateGroup.class})
    @Size(max = 255, message = "櫃位名稱: 長度不能超過255個字元", groups = {RegisterGroup.class, UpdateGroup.class})
    private String counterCName;

    
	// @ManyToOne  (雙向多對一/一對多) 的多對一
	//【此處預設為 @ManyToOne(fetch=FetchType.EAGER) --> 是指 lazy="false"之意】【注意: 此處的預設值與XML版 (p.127及p.132) 的預設值相反】
	//【如果修改為 @ManyToOne(fetch=FetchType.LAZY)  --> 則指 lazy="true" 之意】
    @ManyToOne
    @JoinColumn(name = "COUNTERTYPENO", nullable = false)
    private CounterTypeVO counterTypeVO;
	
	

    @Column(name = "COUNTERINFORM")
    @Size(max = 255, message = "櫃位資訊介紹: 長度不能超過255個字元")
    private String counterInform;

    @Lob
    @Column(name = "COUNTERPIC")
    private byte[] counterPic;

    @Column(name = "COUNTERSTATUS", nullable = false )
    @NotNull(message = "櫃位狀態: 請勿空白")
    @Min(value = 0, message = "櫃位狀態: 必須為0、1或2")
    @Max(value = 2, message = "櫃位狀態: 必須為0、1或2")
    private Integer counterStatus = 1;

    // Constructors
    public CounterVO() {
    }

    // Getters and Setters
    public Integer getCounterNo() {
        return counterNo;
    }

    public void setCounterNo(Integer counterNo) {
        this.counterNo = counterNo;
    }

    public String getCounterAccount() {
        return counterAccount;
    }

    public void setCounterAccount(String counterAccount) {
        this.counterAccount = counterAccount;
    }

    public String getCounterName() {
        return counterName;
    }

    public void setCounterName(String counterName) {
        this.counterName = counterName;
    }

    public String getCounterPassword() {
        return counterPassword;
    }

    public void setCounterPassword(String counterPassword) {
        this.counterPassword = counterPassword;
    }

    public String getCounterAddress() {
        return counterAddress;
    }

    public void setCounterAddress(String counterAddress) {
        this.counterAddress = counterAddress;
    }

    public String getCounterPhone() {
        return counterPhone;
    }

    public void setCounterPhone(String counterPhone) {
        this.counterPhone = counterPhone;
    }

    public String getCounterUid() {
        return counterUid;
    }

    public void setCounterUid(String counterUid) {
        this.counterUid = counterUid;
    }

    public String getCounterEmail() {
        return counterEmail;
    }

    public void setCounterEmail(String counterEmail) {
        this.counterEmail = counterEmail;
    }

    public String getCounterUbn() {
        return counterUbn;
    }

    public void setCounterUbn(String counterUbn) {
        this.counterUbn = counterUbn;
    }

    public String getCounterCName() {
        return counterCName;
    }

    public void setCounterCName(String counterCName) {
        this.counterCName = counterCName;
    }

    public CounterTypeVO getCounterTypeVO() {
        return counterTypeVO;
    }

    public void setCounterTypeVO(CounterTypeVO counterTypeVO) {
        this.counterTypeVO = counterTypeVO;
    }

    public String getCounterInform() {
        return counterInform;
    }

    public void setCounterInform(String counterInform) {
        this.counterInform = counterInform;
    }

    public byte[] getCounterPic() {
        return counterPic;
    }

    public void setCounterPic(byte[] counterPic) {
        this.counterPic = counterPic;
    }

    public Integer getCounterStatus() {
        return counterStatus;
    }

    public void setCounterStatus(Integer counterStatus) {
        this.counterStatus = counterStatus;
    }


}