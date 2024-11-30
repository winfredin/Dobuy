package com.managerauth.model;

import java.io.Serializable;

public class ManagerAuthNo implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer managerNo;
	private Integer authNo;
	
	public Integer getManagerNo() {
		return managerNo;
	}
	public void setManagerNo(Integer managerNo) {
		this.managerNo = managerNo;
	}
	public Integer getAuthNo() {
		return authNo;
	}
	public void setAuthNo(Integer authNo) {
		this.authNo = authNo;
	}
	@Override
    public int hashCode() {
        return managerNo.hashCode() + authNo.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ManagerAuthNo that = (ManagerAuthNo) obj;
        return managerNo.equals(that.managerNo) && authNo.equals(that.authNo);
    }
	
}
