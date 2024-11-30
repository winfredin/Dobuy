package com.auth.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.managerauth.model.ManagerAuthVO;

@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "auth")
public class AuthVO implements Serializable{
    private Integer authNo;
    private String authTitle;
    private String authContext;
    
	
    private Set<ManagerAuthVO> auths;

	// Getters and Setters
	@Id //@Id代表這個屬性是這個Entity的唯一識別屬性，並且對映到Table的主鍵 
	@Column(name = "authNo")  //@Column指這個屬性是對應到資料庫Table的哪一個欄位   //【非必要，但當欄位名稱與屬性名稱不同時則一定要用】
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getAuthNo() {
		 if (this.authNo == null) {
		        // 可能需要抛出某种自定义异常或返回默认值
		        return -1;  // 根据需要修改逻辑
		    }
        return authNo;
    }

    public void setAuthNo(int authNo) {
        this.authNo = authNo;
    }
    @Column(name = "authTitle")
	@NotEmpty(message="權限名稱: 請勿空白")
	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9)]{2,10}$", message = "狀態: 只能是中、英文字母、數字和 , 且長度必需在2到10之間")
    public String getAuthTitle() {
        return authTitle;
    }

    public void setAuthTitle(String authTitle) {
        this.authTitle = authTitle;
    }
    @Column(name = "authContext")
	@NotEmpty(message="權限內容: 請勿空白")
	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9)]{2,10}$", message = "狀態: 只能是中、英文字母、數字和 , 且長度必需在2到10之間")
    public String getAuthContext() {
		return authContext;
	}

	public void setAuthContext(String authContext) {
		this.authContext = authContext;
	}
	
	@OneToMany(mappedBy = "authNo",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	public Set<ManagerAuthVO> getAuthset() {
		return auths;
	}

	public void setAuthset(Set<ManagerAuthVO> auths) {
		this.auths = auths;
	}
}
