package com.manager.model;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


import com.managerauth.model.ManagerAuthVO;

@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "manager")
public class ManagerVO implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private Integer managerNo;
	private String managerName;
	private String managerAccount;
	private String managerPassword;
	private Integer managerstatus;
	
	
	
	private List<ManagerAuthVO> auths;
	
	@OneToMany(mappedBy = "managerNo",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	public List<ManagerAuthVO> getAuths() {
		return auths;
	}
	public void setAuths(List<ManagerAuthVO> auths) {
		this.auths= auths;
	}
	@Id //@Id代表這個屬性是這個Entity的唯一識別屬性，並且對映到Table的主鍵 
	@Column(name = "managerNo")  //@Column指這個屬性是對應到資料庫Table的哪一個欄位   //【非必要，但當欄位名稱與屬性名稱不同時則一定要用】
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getManagerNo() {
		return managerNo;
	}
	public void setManagerNo(Integer managerNo) {
		this.managerNo = managerNo;
	}
	
	
	@NotEmpty(message="管理員名字: 請勿空白")
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	@Column(name = "managerAccount")
	@NotEmpty(message="管理員帳號: 請勿空白")
	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$", message = "管理員帳號: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間")
	public String getManagerAccount() {
		return managerAccount;
	}
	public void setManagerAccount(String managerAccount) {
		this.managerAccount = managerAccount;
	}
	@Column(name = "managerPassword")
	@NotEmpty(message="管理員密碼: 請勿空白")
	@Pattern(regexp = "^[(a-zA-Z0-9_)]{2,10}$", message = "管理員姓名: 只能是英文字母、數字和_ , 且長度必需在2到10之間")
	public String getManagerPassword() {
		return managerPassword;
	}
	public void setManagerPassword(String managerPassword) {
		this.managerPassword = managerPassword;
	}
	
	@Transient
	public String getManagerstatusDisplay() {
	    // 如果 managerstatus 為 1，顯示「在職」，否則顯示「離職」
	    return managerstatus != null && managerstatus == 1 ? "在職" : "離職";
	}
	
	@Column(name = "managerStatus")
	@NotNull(message="管理員狀態: 請勿空白")
	@Min(0)
	@Max(1)
	public Integer getManagerstatus() {
		return this.managerstatus;
	}
	public void setManagerstatus(Integer managerstatus) {
	    // 設定數字狀態
	    this.managerstatus = managerstatus;
	
	}
	
}
