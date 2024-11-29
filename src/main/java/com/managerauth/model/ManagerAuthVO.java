	package com.managerauth.model;
	
	import java.io.Serializable;
	
	import javax.persistence.Entity;
	import javax.persistence.Id;
	import javax.persistence.IdClass;
	import javax.persistence.JoinColumn;
	import javax.persistence.ManyToOne;
	import javax.persistence.Table;
	
	import com.auth.model.AuthVO;
	import com.manager.model.ManagerVO;
	
	@Entity
	@Table(name="managerauth")
	@IdClass(ManagerAuthNo.class)
	public class ManagerAuthVO implements Serializable {
	
	    private static final long serialVersionUID = 1L;
	    @Id
	    @ManyToOne
	    @JoinColumn(name="managerNo",nullable = false)
	    private ManagerVO managerNo;
	   
	    @Id
	    @ManyToOne
	    @JoinColumn(name="authNo",nullable = false)
	    private AuthVO authNo;
	
		public ManagerVO getManagerNo() {
			return managerNo;
		}
	
		public void setManagerNo(ManagerVO managerNo) {
			this.managerNo = managerNo;
		}
	
		public AuthVO getAuthNo() {
			return authNo;
		}
	
		public void setAuthNo(AuthVO authNo) {
			this.authNo = authNo;
		}
	
		public ManagerAuthVO() {
			
		}
	
	}
